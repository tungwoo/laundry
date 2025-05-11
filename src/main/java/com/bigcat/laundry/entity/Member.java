package com.bigcat.laundry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员实体类
 */
@Data
@TableName("t_member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 会员卡余额（正常余额）
     */
    private BigDecimal balance;
    
    /**
     * 会员卡赠送余额
     */
    private BigDecimal giftBalance;

    /**
     * 会员积分
     */
    private Integer points;

    /**
     * 会员等级（1-普通会员，2-银卡会员，3-金卡会员）
     */
    private Integer level;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0-否，1-是）
     */
    private Integer deleted;
} 