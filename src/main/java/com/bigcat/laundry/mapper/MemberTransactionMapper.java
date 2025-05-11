package com.bigcat.laundry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bigcat.laundry.entity.MemberTransaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员账户流水Mapper接口
 */
@Mapper
public interface MemberTransactionMapper extends BaseMapper<MemberTransaction> {
} 