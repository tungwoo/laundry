package com.bigcat.laundry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 */
@Data
@TableName("t_order")
public class Order {

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 会员ID（非会员为空）
     */
    private Long customerId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String customerPhone;

    /**
     * 订单状态（PENDING-待处理, PROCESSING-处理中, READY-待取件, COMPLETED-已完成, CANCELLED-已取消）
     */
    private String status;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 支付方式（UNPAID-未支付, CASH-现金, WECHAT-微信支付, ALIPAY-支付宝, MEMBER_CARD-会员卡）
     */
    private String paymentMethod;

    /**
     * 是否已支付
     */
    private Boolean isPaid;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 预计完成时间
     */
    private LocalDateTime estimatedCompletionTime;

    /**
     * 实际完成时间
     */
    private LocalDateTime actualCompletionTime;

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
     * 门店ID
     */
    private Long storeId;

    /**
     * 删除标记（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 衣物明细列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<OrderItem> orderItems;
} 