package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.entity.OrderStatusLog;
import com.bigcat.laundry.mapper.OrderStatusLogMapper;
import com.bigcat.laundry.service.OrderStatusLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单状态日志Service实现类
 */
@Service
public class OrderStatusLogServiceImpl extends ServiceImpl<OrderStatusLogMapper, OrderStatusLog> implements OrderStatusLogService {

    @Override
    public boolean addLog(Long orderId, String orderNo, Integer beforeStatus, Integer afterStatus,
                        String description, Long operatorId, String operatorName, String remark) {
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderId);
        log.setOrderNo(orderNo);
        log.setBeforeStatus(beforeStatus);
        log.setAfterStatus(afterStatus);
        log.setDescription(description);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperateTime(LocalDateTime.now());
        log.setRemark(remark);
        return save(log);
    }

    @Override
    public List<OrderStatusLog> listByOrderNo(String orderNo) {
        return list(new LambdaQueryWrapper<OrderStatusLog>()
                .eq(OrderStatusLog::getOrderNo, orderNo)
                .orderByDesc(OrderStatusLog::getOperateTime));
    }

    @Override
    public Page<OrderStatusLog> page(Page<OrderStatusLog> page, String orderNo) {
        return page(page, new LambdaQueryWrapper<OrderStatusLog>()
                .eq(OrderStatusLog::getOrderNo, orderNo)
                .orderByDesc(OrderStatusLog::getOperateTime));
    }
} 