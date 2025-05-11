package com.bigcat.laundry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员账户流水表实体类
 */
@Data
@TableName("t_member_transaction")
public class MemberTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员姓名
     */
    private String memberName;

    /**
     * 交易类型（1-充值，2-消费，3-退款，4-赠送）
     */
    private Integer type;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易前余额（正常余额）
     */
    private BigDecimal beforeBalance;

    /**
     * 交易后余额（正常余额）
     */
    private BigDecimal afterBalance;
    
    /**
     * 交易前赠送余额
     */
    private BigDecimal beforeGiftBalance;
    
    /**
     * 交易后赠送余额
     */
    private BigDecimal afterGiftBalance;

    /**
     * 关联订单号
     */
    private String orderNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 