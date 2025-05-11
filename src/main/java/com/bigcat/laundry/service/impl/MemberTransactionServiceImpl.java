package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.entity.MemberTransaction;
import com.bigcat.laundry.mapper.MemberTransactionMapper;
import com.bigcat.laundry.service.MemberTransactionService;
import org.springframework.stereotype.Service;

/**
 * 会员交易流水Service实现类
 */
@Service
public class MemberTransactionServiceImpl extends ServiceImpl<MemberTransactionMapper, MemberTransaction> implements MemberTransactionService {

    @Override
    public Page<MemberTransaction> page(Page<MemberTransaction> page, Long memberId, Integer type) {
        LambdaQueryWrapper<MemberTransaction> wrapper = new LambdaQueryWrapper<MemberTransaction>()
                .eq(memberId != null, MemberTransaction::getMemberId, memberId)
                .eq(type != null, MemberTransaction::getType, type)
                .orderByDesc(MemberTransaction::getCreateTime);
        return page(page, wrapper);
    }
} 