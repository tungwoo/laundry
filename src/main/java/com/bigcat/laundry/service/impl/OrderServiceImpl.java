package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.common.exception.BusinessException;
import com.bigcat.laundry.common.util.OrderNoGenerator;
import com.bigcat.laundry.entity.Member;
import com.bigcat.laundry.entity.Order;
import com.bigcat.laundry.entity.OrderDetail;
import com.bigcat.laundry.mapper.OrderMapper;
import com.bigcat.laundry.service.*;
import com.bigcat.laundry.vo.OrderCreateVO;
import com.bigcat.laundry.vo.OrderDetailVO;
import com.bigcat.laundry.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单Service实现类
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderDetailService orderDetailService;
    private final OrderStatusLogService orderStatusLogService;
    private final MemberService memberService;
    private final ClothesTypeService clothesTypeService;

    @Override
    public Page<Order> page(Page<Order> page, String orderNo, String memberPhone, Integer status,
                           LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getDeleted, 0)
                .like(StringUtils.hasText(orderNo), Order::getOrderNo, orderNo)
                .like(StringUtils.hasText(memberPhone), Order::getMemberPhone, memberPhone)
                .eq(status != null, Order::getStatus, status)
                .ge(startTime != null, Order::getCreateTime, startTime)
                .le(endTime != null, Order::getCreateTime, endTime)
                .orderByDesc(Order::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(OrderCreateVO orderCreateVO) {
        // 生成订单编号
        String orderNo = OrderNoGenerator.generate();
        
        // 生成取衣码
        String pickupCode = OrderNoGenerator.generatePickupCode();
        
        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setMemberId(orderCreateVO.getMemberId());
        order.setMemberName(orderCreateVO.getMemberName());
        order.setMemberPhone(orderCreateVO.getMemberPhone());
        order.setTotalAmount(orderCreateVO.getTotalAmount());
        order.setPayAmount(orderCreateVO.getPayAmount());
        order.setPayStatus(0); // 初始为未支付
        order.setStatus(1); // 初始状态为待清洗
        order.setRemark(orderCreateVO.getRemark());
        order.setClothesCount(orderCreateVO.getDetailList().size());
        order.setReceiveTime(LocalDateTime.now());
        order.setEstimateTime(orderCreateVO.getEstimateTime());
        order.setPickupCode(pickupCode);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(0);
        
        // 保存订单
        save(order);
        
        // 保存订单明细
        List<OrderDetail> detailList = new ArrayList<>();
        for (OrderDetailVO detailVO : orderCreateVO.getDetailList()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setOrderNo(orderNo);
            detail.setClothesTypeId(detailVO.getClothesTypeId());
            detail.setClothesTypeName(detailVO.getClothesTypeName());
            detail.setColor(detailVO.getColor());
            detail.setBrand(detailVO.getBrand());
            detail.setMaterial(detailVO.getMaterial());
            detail.setPrice(detailVO.getPrice());
            detail.setQuantity(detailVO.getQuantity());
            detail.setAmount(detailVO.getAmount());
            detail.setPhoto(detailVO.getPhoto());
            detail.setFlawPhotos(detailVO.getFlawPhotos());
            detail.setStainMark(detailVO.getStainMark());
            detail.setSpecialRequest(detailVO.getSpecialRequest());
            detail.setIsUrgent(detailVO.getIsUrgent());
            detail.setIsColorProtect(detailVO.getIsColorProtect());
            detail.setIsSpecial(detailVO.getIsSpecial());
            detail.setRemark(detailVO.getRemark());
            detail.setCreateTime(LocalDateTime.now());
            detail.setUpdateTime(LocalDateTime.now());
            detailList.add(detail);
        }
        orderDetailService.saveBatch(detailList);
        
        // 记录订单状态日志
        orderStatusLogService.addLog(
                order.getId(), orderNo, null, 1, 
                "创建订单", null, "系统", null);
        
        // 如果是会员下单，且选择了会员卡支付，则立即扣款
        if (order.getMemberId() != null && orderCreateVO.getPayType() != null && orderCreateVO.getPayType() == 3) {
            payOrder(orderNo, 3, null, "系统");
        }
        
        // 返回订单信息
        OrderVO orderVO = new OrderVO();
        orderVO.setOrder(order);
        orderVO.setDetailList(detailList);
        return orderVO;
    }

    @Override
    public OrderVO getOrderDetail(String orderNo) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        List<OrderDetail> detailList = orderDetailService.listByOrderNo(orderNo);
        
        OrderVO orderVO = new OrderVO();
        orderVO.setOrder(order);
        orderVO.setDetailList(detailList);
        return orderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderNo, Long operatorId, String operatorName) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 已完成的订单不能取消
        if (order.getStatus() == 5 || order.getStatus() == 6) {
            throw new BusinessException("订单已完成，不能取消");
        }
        
        // 已取消的订单不能再次取消
        if (order.getStatus() == 7) {
            throw new BusinessException("订单已取消，不能重复操作");
        }
        
        Integer beforeStatus = order.getStatus();
        
        // 更新订单状态为已取消
        order.setStatus(7);
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            orderStatusLogService.addLog(
                    order.getId(), orderNo, beforeStatus, 7,
                    "取消订单", operatorId, operatorName, null);
            
            // 如果订单已支付，则退款
            if (order.getPayStatus() == 1) {
                refundOrder(orderNo, operatorId, operatorName);
            }
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(String orderNo, Integer status, Long operatorId, String operatorName) {
        if (status < 1 || status > 7) {
            throw new BusinessException("状态值不正确");
        }
        
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 订单状态流转检查
        Integer beforeStatus = order.getStatus();
        checkStatusTransition(beforeStatus, status);
        
        // 更新订单状态
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            String description = getStatusChangeDescription(status);
            orderStatusLogService.addLog(
                    order.getId(), orderNo, beforeStatus, status,
                    description, operatorId, operatorName, null);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean shelfOrder(String orderNo, String shelfLocation, String shelfNumber, 
                            Long operatorId, String operatorName) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 只有待上架状态的订单才能上架
        if (order.getStatus() != 3) {
            throw new BusinessException("订单状态不是待上架，不能上架");
        }
        
        Integer beforeStatus = order.getStatus();
        
        // 更新订单上架信息和状态
        order.setStatus(4); // 已上架
        order.setShelfLocation(shelfLocation);
        order.setShelfNumber(shelfNumber);
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            orderStatusLogService.addLog(
                    order.getId(), orderNo, beforeStatus, 4,
                    "订单上架", operatorId, operatorName, 
                    "上架位置：" + shelfLocation + "，上架号：" + shelfNumber);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pickupOrder(String orderNo, String pickupCode, Long operatorId, String operatorName) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 只有已上架状态的订单才能自取
        if (order.getStatus() != 4) {
            throw new BusinessException("订单状态不是已上架，不能自取");
        }
        
        // 校验取衣码
        if (!order.getPickupCode().equals(pickupCode)) {
            throw new BusinessException("取衣码不正确");
        }
        
        // 校验订单是否已支付
        if (order.getPayStatus() != 1) {
            throw new BusinessException("订单未支付，不能取衣");
        }
        
        Integer beforeStatus = order.getStatus();
        
        // 更新订单状态为已自取
        order.setStatus(5); // 已自取
        order.setPickupTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            orderStatusLogService.addLog(
                    order.getId(), orderNo, beforeStatus, 5,
                    "顾客自取", operatorId, operatorName, null);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deliveryOrder(String orderNo, String deliveryAddress, String deliveryPhoto, 
                               Long operatorId, String operatorName) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 只有已上架状态的订单才能配送
        if (order.getStatus() != 4) {
            throw new BusinessException("订单状态不是已上架，不能配送");
        }
        
        // 校验订单是否已支付
        if (order.getPayStatus() != 1) {
            throw new BusinessException("订单未支付，不能配送");
        }
        
        Integer beforeStatus = order.getStatus();
        
        // 更新订单状态为已配送
        order.setStatus(6); // 已送上门
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryPhoto(deliveryPhoto);
        order.setPickupTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            orderStatusLogService.addLog(
                    order.getId(), orderNo, beforeStatus, 6,
                    "配送上门", operatorId, operatorName, 
                    "配送地址：" + deliveryAddress);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(String orderNo, Integer payType, Long operatorId, String operatorName) {
        if (payType < 1 || payType > 3) {
            throw new BusinessException("支付方式不正确");
        }
        
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 已支付的订单不能重复支付
        if (order.getPayStatus() == 1) {
            throw new BusinessException("订单已支付，不能重复支付");
        }
        
        // 如果是会员卡支付，校验会员余额
        if (payType == 3) {
            if (order.getMemberId() == null) {
                throw new BusinessException("非会员不能使用会员卡支付");
            }
            
            Member member = memberService.getById(order.getMemberId());
            if (member == null) {
                throw new BusinessException("会员不存在");
            }
            
            BigDecimal totalBalance = member.getBalance().add(member.getGiftBalance());
            if (totalBalance.compareTo(order.getPayAmount()) < 0) {
                throw new BusinessException("会员余额不足");
            }
            
            // 扣减会员余额
            memberService.consume(
                    order.getMemberId(), 
                    order.getPayAmount(), 
                    orderNo, 
                    operatorId, 
                    operatorName);
        }
        
        // 更新订单支付信息
        order.setPayStatus(1); // 已支付
        order.setPayType(payType);
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            String payTypeDesc = payType == 1 ? "微信支付" : (payType == 2 ? "现金" : "会员卡");
            orderStatusLogService.addLog(
                    order.getId(), orderNo, order.getStatus(), order.getStatus(),
                    "订单支付", operatorId, operatorName, 
                    "支付方式：" + payTypeDesc + "，支付金额：" + order.getPayAmount() + "元");
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refundOrder(String orderNo, Long operatorId, String operatorName) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getDeleted, 0));
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 未支付的订单不能退款
        if (order.getPayStatus() != 1) {
            throw new BusinessException("订单未支付，不能退款");
        }
        
        // 已退款的订单不能重复退款
        if (order.getPayStatus() == 2) {
            throw new BusinessException("订单已退款，不能重复退款");
        }
        
        // 如果是会员卡支付，退回会员余额
        if (order.getPayType() == 3 && order.getMemberId() != null) {
            memberService.refund(
                    order.getMemberId(), 
                    order.getPayAmount(), 
                    orderNo, 
                    operatorId, 
                    operatorName);
        }
        
        // 更新订单支付状态为已退款
        order.setPayStatus(2);
        order.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(order);
        
        if (success) {
            // 记录订单状态变更日志
            orderStatusLogService.addLog(
                    order.getId(), orderNo, order.getStatus(), order.getStatus(),
                    "订单退款", operatorId, operatorName, 
                    "退款金额：" + order.getPayAmount() + "元");
        }
        
        return success;
    }
    
    /**
     * 检查订单状态流转是否合法
     * 
     * @param beforeStatus 当前状态
     * @param afterStatus 目标状态
     */
    private void checkStatusTransition(Integer beforeStatus, Integer afterStatus) {
        // 订单状态：1-待清洗，2-清洗中，3-待上架，4-已上架，5-已自取，6-已送上门，7-已取消
        switch (beforeStatus) {
            case 1: // 待清洗
                if (afterStatus != 2 && afterStatus != 7) {
                    throw new BusinessException("待清洗状态只能流转至清洗中或已取消");
                }
                break;
            case 2: // 清洗中
                if (afterStatus != 3 && afterStatus != 7) {
                    throw new BusinessException("清洗中状态只能流转至待上架或已取消");
                }
                break;
            case 3: // 待上架
                if (afterStatus != 4 && afterStatus != 7) {
                    throw new BusinessException("待上架状态只能流转至已上架或已取消");
                }
                break;
            case 4: // 已上架
                if (afterStatus != 5 && afterStatus != 6 && afterStatus != 7) {
                    throw new BusinessException("已上架状态只能流转至已自取、已送上门或已取消");
                }
                break;
            case 5: // 已自取
            case 6: // 已送上门
                throw new BusinessException("已完成的订单不能变更状态");
            case 7: // 已取消
                throw new BusinessException("已取消的订单不能变更状态");
            default:
                throw new BusinessException("订单状态不正确");
        }
    }
    
    /**
     * 获取状态变更描述
     * 
     * @param status 状态值
     * @return 描述
     */
    private String getStatusChangeDescription(Integer status) {
        switch (status) {
            case 1:
                return "待清洗";
            case 2:
                return "清洗中";
            case 3:
                return "待上架";
            case 4:
                return "已上架";
            case 5:
                return "已自取";
            case 6:
                return "已送上门";
            case 7:
                return "已取消";
            default:
                return "未知状态";
        }
    }
} 