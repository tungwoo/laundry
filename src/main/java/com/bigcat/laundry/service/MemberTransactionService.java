package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.MemberTransaction;

/**
 * 会员交易流水Service接口
 */
public interface MemberTransactionService extends IService<MemberTransaction> {

    /**
     * 分页查询会员交易流水
     *
     * @param page     分页参数
     * @param memberId 会员ID
     * @param type     交易类型（1-充值，2-消费，3-退款）
     * @return 会员交易流水分页列表
     */
    Page<MemberTransaction> page(Page<MemberTransaction> page, Long memberId, Integer type);
} 