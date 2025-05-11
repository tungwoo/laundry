package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 订单上架VO类
 */
@Data
public class OrderShelfVO {

    /**
     * 订单编号
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderNo;

    /**
     * 上架位置
     */
    @NotEmpty(message = "上架位置不能为空")
    private String shelfLocation;

    /**
     * 上架号
     */
    @NotEmpty(message = "上架号不能为空")
    private String shelfNumber;

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