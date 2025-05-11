package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.Member;

import java.math.BigDecimal;

/**
 * 会员Service接口
 */
public interface MemberService extends IService<Member> {

    /**
     * 分页查询会员列表
     *
     * @param page  分页参数
     * @param name  会员姓名，可选
     * @param phone 手机号，可选
     * @return 会员分页列表
     */
    Page<Member> page(Page<Member> page, String name, String phone);

    /**
     * 根据微信openid查询会员
     *
     * @param openid 微信openid
     * @return 会员信息
     */
    Member getByOpenid(String openid);

    /**
     * 会员充值（不包含赠送余额）
     *
     * @param id     会员ID
     * @param amount 充值金额
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean recharge(Long id, BigDecimal amount, Long operatorId, String operatorName);

    /**
     * 会员充值（包含赠送余额）
     *
     * @param id     会员ID
     * @param amount 充值金额
     * @param giftAmount 赠送金额
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean rechargeWithGift(Long id, BigDecimal amount, BigDecimal giftAmount, Long operatorId, String operatorName);

    /**
     * 会员消费
     *
     * @param id     会员ID
     * @param amount 消费金额
     * @param orderNo 订单编号
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean consume(Long id, BigDecimal amount, String orderNo, Long operatorId, String operatorName);

    /**
     * 会员退款
     *
     * @param id     会员ID
     * @param amount 退款金额
     * @param orderNo 订单编号
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean refund(Long id, BigDecimal amount, String orderNo, Long operatorId, String operatorName);
} 