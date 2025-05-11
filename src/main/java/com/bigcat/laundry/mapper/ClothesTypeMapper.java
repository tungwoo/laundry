package com.bigcat.laundry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bigcat.laundry.entity.ClothesType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 衣物类型Mapper接口
 */
@Mapper
public interface ClothesTypeMapper extends BaseMapper<ClothesType> {
} 