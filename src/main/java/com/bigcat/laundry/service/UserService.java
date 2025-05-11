package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    User getByUsername(String username);
    
    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户对象
     */
    User getByPhone(String phone);
    
    /**
     * 根据微信openid查询用户
     *
     * @param openid 微信openid
     * @return 用户对象
     */
    User getByOpenid(String openid);
    
    /**
     * 创建微信用户
     *
     * @param openid 微信openid
     * @param phone 手机号
     * @param nickname 昵称
     * @param avatar 头像URL
     * @return 用户对象
     */
    User createWechatUser(String openid, String phone, String nickname, String avatar);
    
    /**
     * 更新用户余额
     *
     * @param userId 用户ID
     * @param amount 变动金额（正数增加，负数减少）
     * @return 是否成功
     */
    boolean updateBalance(Long userId, java.math.BigDecimal amount);
    
    /**
     * 更新用户积分
     *
     * @param userId 用户ID
     * @param points 变动积分（正数增加，负数减少）
     * @return 是否成功
     */
    boolean updatePoints(Long userId, Integer points);
} 