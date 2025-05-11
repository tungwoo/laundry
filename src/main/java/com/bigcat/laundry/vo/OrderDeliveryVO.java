package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 订单配送VO类
 */
@Data
public class OrderDeliveryVO {

    /**
     * 订单编号
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderNo;

    /**
     * 配送地址
     */
    @NotEmpty(message = "配送地址不能为空")
    private String deliveryAddress;

    /**
     * 配送照片
     */
    private String deliveryPhoto;

    /**
     * 操作人ID
     */
    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @NotEmpty(message = "操作人姓名不能为空")
    private String operatorName;
} 