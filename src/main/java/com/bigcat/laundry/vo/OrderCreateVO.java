package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单创建VO类
 */
@Data
public class OrderCreateVO {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员姓名
     */
    private String memberName;

    /**
     * 会员手机号
     */
    @NotEmpty(message = "手机号不能为空")
    private String memberPhone;

    /**
     * 订单总金额
     */
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    @NotNull(message = "实付金额不能为空")
    private BigDecimal payAmount;

    /**
     * 支付方式（1-微信支付，2-现金，3-会员卡）
     */
    private Integer payType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 预计取衣时间
     */
    private LocalDateTime estimateTime;

    /**
     * 衣物明细列表
     */
    @NotEmpty(message = "衣物明细不能为空")
    @Valid
    private List<OrderDetailVO> detailList;
} 