package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.entity.User;
import com.bigcat.laundry.mapper.UserMapper;
import com.bigcat.laundry.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .eq(User::getDeleted, 0);
        return getOne(wrapper);
    }

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户对象
     */
    @Override
    public User getByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone)
                .eq(User::getDeleted, 0);
        return getOne(wrapper);
    }

    /**
     * 根据微信openid查询用户
     *
     * @param openid 微信openid
     * @return 用户对象
     */
    @Override
    public User getByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid)
                .eq(User::getDeleted, 0);
        return getOne(wrapper);
    }

    /**
     * 创建微信用户
     *
     * @param openid 微信openid
     * @param phone 手机号
     * @param nickname 昵称
     * @param avatar 头像URL
     * @return 用户对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createWechatUser(String openid, String phone, String nickname, String avatar) {
        User user = new User();
        user.setOpenid(openid);
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setAvatar(avatar);
        user.setBalance(BigDecimal.ZERO);
        user.setPoints(0);
        user.setUserType(0); // 普通用户
        user.setStatus(1); // 正常状态
        user.setDeleted(0); // 未删除
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        save(user);
        log.info("创建微信用户成功: {}", user);
        return user;
    }

    /**
     * 更新用户余额
     *
     * @param userId 用户ID
     * @param amount 变动金额（正数增加，负数减少）
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBalance(Long userId, BigDecimal amount) {
        User user = getById(userId);
        if (user == null) {
            log.error("更新余额失败，用户不存在: userId={}", userId);
            return false;
        }
        
        BigDecimal newBalance = user.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("更新余额失败，余额不足: userId={}, currentBalance={}, amount={}", userId, user.getBalance(), amount);
            return false;
        }
        
        user.setBalance(newBalance);
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);
        log.info("更新用户余额: userId={}, 变动金额={}, 新余额={}, 结果={}", userId, amount, newBalance, result);
        return result;
    }

    /**
     * 更新用户积分
     *
     * @param userId 用户ID
     * @param points 变动积分（正数增加，负数减少）
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePoints(Long userId, Integer points) {
        User user = getById(userId);
        if (user == null) {
            log.error("更新积分失败，用户不存在: userId={}", userId);
            return false;
        }
        
        int newPoints = user.getPoints() + points;
        if (newPoints < 0) {
            log.error("更新积分失败，积分不足: userId={}, currentPoints={}, points={}", userId, user.getPoints(), points);
            return false;
        }
        
        user.setPoints(newPoints);
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);
        log.info("更新用户积分: userId={}, 变动积分={}, 新积分={}, 结果={}", userId, points, newPoints, result);
        return result;
    }
} 