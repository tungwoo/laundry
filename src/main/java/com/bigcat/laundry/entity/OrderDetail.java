package com.bigcat.laundry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细表实体类
 */
@Data
@TableName("t_order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 衣物类型ID
     */
    private Long clothesTypeId;

    /**
     * 衣物类型名称
     */
    private String clothesTypeName;

    /**
     * 衣物颜色
     */
    private String color;
    
    /**
     * 衣物品牌
     */
    private String brand;

    /**
     * 衣物材质
     */
    private String material;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 衣物照片
     */
    private String photo;

    /**
     * 衣物瑕疵照片，多张用逗号分隔
     */
    private String flawPhotos;

    /**
     * 污渍标记
     */
    private String stainMark;

    /**
     * 特殊要求
     */
    private String specialRequest;

    /**
     * 是否加急（0-否，1-是）
     */
    private Integer isUrgent;

    /**
     * 是否防褪色（0-否，1-是）
     */
    private Integer isColorProtect;

    /**
     * 是否特殊处理（0-否，1-是）
     */
    private Integer isSpecial;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 