package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.Order;
import com.bigcat.laundry.vo.OrderCreateVO;
import com.bigcat.laundry.vo.OrderVO;

import java.time.LocalDateTime;

/**
 * 订单Service接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 分页查询订单列表
     *
     * @param page        分页参数
     * @param orderNo     订单编号，可选
     * @param memberPhone 会员手机号，可选
     * @param status      订单状态，可选
     * @param startTime   开始时间，可选
     * @param endTime     结束时间，可选
     * @return 订单分页列表
     */
    Page<Order> page(Page<Order> page, String orderNo, String memberPhone, Integer status, 
                     LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 创建订单
     *
     * @param orderCreateVO 订单创建参数
     * @return 订单信息
     */
    OrderVO createOrder(OrderCreateVO orderCreateVO);

    /**
     * 根据订单编号查询订单详情
     *
     * @param orderNo 订单编号
     * @return 订单详情
     */
    OrderVO getOrderDetail(String orderNo);

    /**
     * 取消订单
     *
     * @param orderNo      订单编号
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean cancelOrder(String orderNo, Long operatorId, String operatorName);

    /**
     * 更新订单状态
     *
     * @param orderNo      订单编号
     * @param status       状态值
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean updateOrderStatus(String orderNo, Integer status, Long operatorId, String operatorName);

    /**
     * 上架订单
     *
     * @param orderNo       订单编号
     * @param shelfLocation 上架位置
     * @param shelfNumber   上架号
     * @param operatorId    操作人ID
     * @param operatorName  操作人姓名
     * @return 是否成功
     */
    boolean shelfOrder(String orderNo, String shelfLocation, String shelfNumber, 
                      Long operatorId, String operatorName);

    /**
     * 顾客自取订单
     *
     * @param orderNo      订单编号
     * @param pickupCode   取衣码
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean pickupOrder(String orderNo, String pickupCode, Long operatorId, String operatorName);

    /**
     * 配送订单
     *
     * @param orderNo        订单编号
     * @param deliveryAddress 配送地址
     * @param deliveryPhoto   配送照片
     * @param operatorId      操作人ID
     * @param operatorName    操作人姓名
     * @return 是否成功
     */
    boolean deliveryOrder(String orderNo, String deliveryAddress, String deliveryPhoto, 
                         Long operatorId, String operatorName);

    /**
     * 支付订单
     *
     * @param orderNo      订单编号
     * @param payType      支付方式（1-微信支付，2-现金，3-会员卡）
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean payOrder(String orderNo, Integer payType, Long operatorId, String operatorName);

    /**
     * 退款订单
     *
     * @param orderNo      订单编号
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean refundOrder(String orderNo, Long operatorId, String operatorName);
} 