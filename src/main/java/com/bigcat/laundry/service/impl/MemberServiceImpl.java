package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.common.exception.BusinessException;
import com.bigcat.laundry.entity.Member;
import com.bigcat.laundry.entity.MemberTransaction;
import com.bigcat.laundry.mapper.MemberMapper;
import com.bigcat.laundry.service.MemberService;
import com.bigcat.laundry.service.MemberTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员Service实现类
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    private final MemberTransactionService memberTransactionService;

    @Override
    public Page<Member> page(Page<Member> page, String name, String phone) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<Member>()
                .eq(Member::getDeleted, 0)
                .like(StringUtils.hasText(name), Member::getName, name)
                .like(StringUtils.hasText(phone), Member::getPhone, phone)
                .orderByDesc(Member::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Member getByOpenid(String openid) {
        return getOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getOpenid, openid)
                .eq(Member::getDeleted, 0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recharge(Long id, BigDecimal amount, Long operatorId, String operatorName) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }

        Member member = getById(id);
        if (member == null) {
            throw new BusinessException("会员不存在");
        }

        BigDecimal beforeBalance = member.getBalance();
        BigDecimal afterBalance = beforeBalance.add(amount);

        member.setBalance(afterBalance);
        member.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(member);

        if (success) {
            // 记录交易流水
            MemberTransaction transaction = new MemberTransaction();
            transaction.setMemberId(id);
            transaction.setMemberName(member.getName());
            transaction.setType(1); // 充值
            transaction.setAmount(amount);
            transaction.setBeforeBalance(beforeBalance);
            transaction.setAfterBalance(afterBalance);
            transaction.setBeforeGiftBalance(member.getGiftBalance());
            transaction.setAfterGiftBalance(member.getGiftBalance());
            transaction.setRemark("会员充值");
            transaction.setOperatorId(operatorId);
            transaction.setOperatorName(operatorName);
            transaction.setCreateTime(LocalDateTime.now());
            memberTransactionService.save(transaction);
        }

        return success;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rechargeWithGift(Long id, BigDecimal amount, BigDecimal giftAmount, Long operatorId, String operatorName) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }

        if (giftAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("赠送金额不能小于0");
        }

        Member member = getById(id);
        if (member == null) {
            throw new BusinessException("会员不存在");
        }

        BigDecimal beforeBalance = member.getBalance();
        BigDecimal afterBalance = beforeBalance.add(amount);
        
        BigDecimal beforeGiftBalance = member.getGiftBalance();
        BigDecimal afterGiftBalance = beforeGiftBalance.add(giftAmount);

        member.setBalance(afterBalance);
        member.setGiftBalance(afterGiftBalance);
        member.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(member);

        if (success) {
            // 记录充值交易流水
            MemberTransaction transaction = new MemberTransaction();
            transaction.setMemberId(id);
            transaction.setMemberName(member.getName());
            transaction.setType(1); // 充值
            transaction.setAmount(amount);
            transaction.setBeforeBalance(beforeBalance);
            transaction.setAfterBalance(afterBalance);
            transaction.setBeforeGiftBalance(beforeGiftBalance);
            transaction.setAfterGiftBalance(beforeGiftBalance); // 充值不影响赠送余额
            transaction.setRemark("会员充值");
            transaction.setOperatorId(operatorId);
            transaction.setOperatorName(operatorName);
            transaction.setCreateTime(LocalDateTime.now());
            memberTransactionService.save(transaction);
            
            // 如果有赠送金额，记录赠送交易流水
            if (giftAmount.compareTo(BigDecimal.ZERO) > 0) {
                MemberTransaction giftTransaction = new MemberTransaction();
                giftTransaction.setMemberId(id);
                giftTransaction.setMemberName(member.getName());
                giftTransaction.setType(4); // 赠送
                giftTransaction.setAmount(giftAmount);
                giftTransaction.setBeforeBalance(afterBalance); // 赠送不影响正常余额
                giftTransaction.setAfterBalance(afterBalance);
                giftTransaction.setBeforeGiftBalance(beforeGiftBalance);
                giftTransaction.setAfterGiftBalance(afterGiftBalance);
                giftTransaction.setRemark("充值赠送");
                giftTransaction.setOperatorId(operatorId);
                giftTransaction.setOperatorName(operatorName);
                giftTransaction.setCreateTime(LocalDateTime.now());
                memberTransactionService.save(giftTransaction);
            }
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean consume(Long id, BigDecimal amount, String orderNo, Long operatorId, String operatorName) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("消费金额必须大于0");
        }

        Member member = getById(id);
        if (member == null) {
            throw new BusinessException("会员不存在");
        }

        BigDecimal totalBalance = member.getBalance().add(member.getGiftBalance());
        
        // 检查总余额是否足够消费
        if (totalBalance.compareTo(amount) < 0) {
            throw new BusinessException("会员余额不足");
        }
        
        BigDecimal beforeBalance = member.getBalance();
        BigDecimal beforeGiftBalance = member.getGiftBalance();
        BigDecimal remainingAmount = amount;
        BigDecimal normalUsed = BigDecimal.ZERO;
        BigDecimal giftUsed = BigDecimal.ZERO;
        
        // 先扣减正常余额
        if (beforeBalance.compareTo(BigDecimal.ZERO) > 0) {
            if (beforeBalance.compareTo(remainingAmount) >= 0) {
                // 正常余额足够支付
                normalUsed = remainingAmount;
                remainingAmount = BigDecimal.ZERO;
            } else {
                // 正常余额不足，全部用完
                normalUsed = beforeBalance;
                remainingAmount = remainingAmount.subtract(beforeBalance);
            }
        }
        
        // 如果还有剩余金额，扣减赠送余额
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            giftUsed = remainingAmount;
        }
        
        // 计算扣减后的余额
        BigDecimal afterBalance = beforeBalance.subtract(normalUsed);
        BigDecimal afterGiftBalance = beforeGiftBalance.subtract(giftUsed);
        
        // 更新会员余额
        member.setBalance(afterBalance);
        member.setGiftBalance(afterGiftBalance);
        member.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(member);

        if (success) {
            // 记录交易流水
            MemberTransaction transaction = new MemberTransaction();
            transaction.setMemberId(id);
            transaction.setMemberName(member.getName());
            transaction.setType(2); // 消费
            transaction.setAmount(amount);
            transaction.setBeforeBalance(beforeBalance);
            transaction.setAfterBalance(afterBalance);
            transaction.setBeforeGiftBalance(beforeGiftBalance);
            transaction.setAfterGiftBalance(afterGiftBalance);
            transaction.setOrderNo(orderNo);
            transaction.setRemark("会员消费（正常余额:" + normalUsed + "，赠送余额:" + giftUsed + "）");
            transaction.setOperatorId(operatorId);
            transaction.setOperatorName(operatorName);
            transaction.setCreateTime(LocalDateTime.now());
            memberTransactionService.save(transaction);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refund(Long id, BigDecimal amount, String orderNo, Long operatorId, String operatorName) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("退款金额必须大于0");
        }

        Member member = getById(id);
        if (member == null) {
            throw new BusinessException("会员不存在");
        }

        // 查询订单相关的消费记录
        LambdaQueryWrapper<MemberTransaction> wrapper = new LambdaQueryWrapper<MemberTransaction>()
                .eq(MemberTransaction::getMemberId, id)
                .eq(MemberTransaction::getOrderNo, orderNo)
                .eq(MemberTransaction::getType, 2); // 消费记录
        
        MemberTransaction consumeRecord = memberTransactionService.getOne(wrapper);
        if (consumeRecord == null) {
            throw new BusinessException("找不到对应的消费记录，无法退款");
        }
        
        // 分析原始消费记录中的备注信息，解析出正常余额和赠送余额的扣减情况
        String remark = consumeRecord.getRemark();
        BigDecimal normalUsed = BigDecimal.ZERO;
        BigDecimal giftUsed = BigDecimal.ZERO;
        
        try {
            if (remark.contains("正常余额:") && remark.contains("赠送余额:")) {
                int normalStart = remark.indexOf("正常余额:") + 5;
                int normalEnd = remark.indexOf("，赠送余额:");
                int giftStart = remark.indexOf("赠送余额:") + 5;
                int giftEnd = remark.indexOf("）");
                
                normalUsed = new BigDecimal(remark.substring(normalStart, normalEnd));
                giftUsed = new BigDecimal(remark.substring(giftStart, giftEnd));
            }
        } catch (Exception e) {
            // 解析失败，按照比例退还
            if (amount.compareTo(consumeRecord.getAmount()) >= 0) {
                // 全额退款
                normalUsed = consumeRecord.getBeforeBalance().subtract(consumeRecord.getAfterBalance());
                giftUsed = consumeRecord.getBeforeGiftBalance().subtract(consumeRecord.getAfterGiftBalance());
            } else {
                // 部分退款，按照原消费比例计算
                BigDecimal totalConsumed = consumeRecord.getAmount();
                BigDecimal ratio = amount.divide(totalConsumed, 10, BigDecimal.ROUND_HALF_UP);
                
                normalUsed = consumeRecord.getBeforeBalance()
                        .subtract(consumeRecord.getAfterBalance())
                        .multiply(ratio)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                
                giftUsed = consumeRecord.getBeforeGiftBalance()
                        .subtract(consumeRecord.getAfterGiftBalance())
                        .multiply(ratio)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        
        // 如果退款金额超过原消费金额，调整为原消费金额
        if (normalUsed.add(giftUsed).compareTo(amount) > 0) {
            BigDecimal totalUsed = normalUsed.add(giftUsed);
            BigDecimal ratio = amount.divide(totalUsed, 10, BigDecimal.ROUND_HALF_UP);
            normalUsed = normalUsed.multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP);
            giftUsed = giftUsed.multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        
        BigDecimal beforeBalance = member.getBalance();
        BigDecimal beforeGiftBalance = member.getGiftBalance();
        
        // 计算退款后的余额
        BigDecimal afterBalance = beforeBalance.add(normalUsed);
        BigDecimal afterGiftBalance = beforeGiftBalance.add(giftUsed);
        
        // 更新会员余额
        member.setBalance(afterBalance);
        member.setGiftBalance(afterGiftBalance);
        member.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(member);

        if (success) {
            // 记录交易流水
            MemberTransaction transaction = new MemberTransaction();
            transaction.setMemberId(id);
            transaction.setMemberName(member.getName());
            transaction.setType(3); // 退款
            transaction.setAmount(amount);
            transaction.setBeforeBalance(beforeBalance);
            transaction.setAfterBalance(afterBalance);
            transaction.setBeforeGiftBalance(beforeGiftBalance);
            transaction.setAfterGiftBalance(afterGiftBalance);
            transaction.setOrderNo(orderNo);
            transaction.setRemark("订单退款（正常余额:" + normalUsed + "，赠送余额:" + giftUsed + "）");
            transaction.setOperatorId(operatorId);
            transaction.setOperatorName(operatorName);
            transaction.setCreateTime(LocalDateTime.now());
            memberTransactionService.save(transaction);
        }

        return success;
    }
} 