package com.bigcat.laundry.vo;

import com.bigcat.laundry.entity.Order;
import com.bigcat.laundry.entity.OrderDetail;
import lombok.Data;

import java.util.List;

/**
 * 订单VO类
 */
@Data
public class OrderVO {

    /**
     * 订单信息
     */
    private Order order;

    /**
     * 订单明细列表
     */
    private List<OrderDetail> detailList;
} 