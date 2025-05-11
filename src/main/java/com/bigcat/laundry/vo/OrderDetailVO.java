package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单明细VO类
 */
@Data
public class OrderDetailVO {

    /**
     * 衣物类型ID
     */
    @NotNull(message = "衣物类型ID不能为空")
    private Long clothesTypeId;

    /**
     * 衣物类型名称
     */
    @NotEmpty(message = "衣物类型名称不能为空")
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
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
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
} 