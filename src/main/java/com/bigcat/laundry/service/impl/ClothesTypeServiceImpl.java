package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.common.exception.BusinessException;
import com.bigcat.laundry.entity.ClothesType;
import com.bigcat.laundry.mapper.ClothesTypeMapper;
import com.bigcat.laundry.service.ClothesTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 衣物类型Service实现类
 */
@Service
public class ClothesTypeServiceImpl extends ServiceImpl<ClothesTypeMapper, ClothesType> implements ClothesTypeService {

    @Override
    public Page<ClothesType> page(Page<ClothesType> page, String name) {
        LambdaQueryWrapper<ClothesType> wrapper = new LambdaQueryWrapper<ClothesType>()
                .eq(ClothesType::getDeleted, 0)
                .like(StringUtils.hasText(name), ClothesType::getName, name)
                .orderByAsc(ClothesType::getSort);
        return page(page, wrapper);
    }

    @Override
    public List<ClothesType> listEnabled() {
        return list(new LambdaQueryWrapper<ClothesType>()
                .eq(ClothesType::getStatus, 1)
                .eq(ClothesType::getDeleted, 0)
                .orderByAsc(ClothesType::getSort));
    }

    @Override
    public ClothesType getByCode(String code) {
        return getOne(new LambdaQueryWrapper<ClothesType>()
                .eq(ClothesType::getCode, code)
                .eq(ClothesType::getDeleted, 0));
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("状态值不正确");
        }

        ClothesType clothesType = getById(id);
        if (clothesType == null) {
            throw new BusinessException("衣物类型不存在");
        }

        clothesType.setStatus(status);
        clothesType.setUpdateTime(LocalDateTime.now());
        return updateById(clothesType);
    }
} 