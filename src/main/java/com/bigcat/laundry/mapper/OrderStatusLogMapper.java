package com.bigcat.laundry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bigcat.laundry.entity.OrderStatusLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单状态日志Mapper接口
 */
@Mapper
public interface OrderStatusLogMapper extends BaseMapper<OrderStatusLog> {
} 