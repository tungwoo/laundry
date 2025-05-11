package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.OrderDetail;

import java.util.List;

/**
 * 订单明细Service接口
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 根据订单ID查询订单明细列表
     *
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    List<OrderDetail> listByOrderId(Long orderId);

    /**
     * 根据订单编号查询订单明细列表
     *
     * @param orderNo 订单编号
     * @return 订单明细列表
     */
    List<OrderDetail> listByOrderNo(String orderNo);
} 