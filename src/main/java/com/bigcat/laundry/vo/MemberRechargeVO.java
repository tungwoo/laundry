package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 会员充值VO类
 */
@Data
public class MemberRechargeVO {

    /**
     * 会员ID
     */
    @NotNull(message = "会员ID不能为空")
    private Long id;

    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0")
    private BigDecimal amount;
    
    /**
     * 赠送金额
     */
    private BigDecimal giftAmount;

    /**
     * 操作人ID
     */
    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @NotNull(message = "操作人姓名不能为空")
    private String operatorName;
} 