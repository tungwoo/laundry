package com.bigcat.laundry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体类
 */
@Data
@TableName("t_order_item")
public class OrderItem {

    /**
     * 订单明细ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 衣物类别ID
     */
    private Long categoryId;

    /**
     * 衣物类别名称
     */
    private String categoryName;

    /**
     * 衣物名称
     */
    private String name;

    /**
     * 衣物颜色
     */
    private String color;

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
     * 特殊处理要求
     */
    private String specialRequirements;

    /**
     * 衣物状态（PENDING-待处理, PROCESSING-处理中, COMPLETED-已完成）
     */
    private String status;

    /**
     * 衣物图片
     */
    private String image;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 删除标记（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;
} 