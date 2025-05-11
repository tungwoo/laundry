package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.ClothesType;

import java.util.List;

/**
 * 衣物类型Service接口
 */
public interface ClothesTypeService extends IService<ClothesType> {

    /**
     * 分页查询衣物类型列表
     *
     * @param page  分页参数
     * @param name  衣物类型名称，可选
     * @return 衣物类型分页列表
     */
    Page<ClothesType> page(Page<ClothesType> page, String name);

    /**
     * 查询所有启用的衣物类型
     *
     * @return 衣物类型列表
     */
    List<ClothesType> listEnabled();

    /**
     * 根据编码查询衣物类型
     *
     * @param code 衣物类型编码
     * @return 衣物类型
     */
    ClothesType getByCode(String code);

    /**
     * 更新衣物类型状态
     *
     * @param id     衣物类型ID
     * @param status 状态（0-禁用，1-启用）
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);
} 