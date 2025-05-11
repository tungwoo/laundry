package com.bigcat.laundry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bigcat.laundry.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员用户Mapper接口
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
} 