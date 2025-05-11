package com.bigcat.laundry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 衣物类型实体类
 */
@Data
@TableName("t_clothes_type")
public class ClothesType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 衣物类型名称
     */
    private String name;

    /**
     * 衣物类型编码
     */
    private String code;

    /**
     * 标准价格
     */
    private BigDecimal price;

    /**
     * 会员价格
     */
    private BigDecimal memberPrice;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

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