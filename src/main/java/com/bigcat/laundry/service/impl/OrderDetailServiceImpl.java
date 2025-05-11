package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.entity.OrderDetail;
import com.bigcat.laundry.mapper.OrderDetailMapper;
import com.bigcat.laundry.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单明细Service实现类
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Override
    public List<OrderDetail> listByOrderId(Long orderId) {
        return list(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, orderId)
                .orderByDesc(OrderDetail::getCreateTime));
    }

    @Override
    public List<OrderDetail> listByOrderNo(String orderNo) {
        return list(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderNo, orderNo)
                .orderByDesc(OrderDetail::getCreateTime));
    }
} 