package com.bigcat.laundry.service;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 管理后台登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    Map<String, Object> adminLogin(String username, String password);

    /**
     * 管理后台退出登录
     *
     * @return 退出结果
     */
    Boolean adminLogout();

    /**
     * 小程序账号密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    Map<String, Object> miniAppLogin(String username, String password);

    /**
     * 小程序微信登录
     *
     * @param code 微信临时登录凭证
     * @return 登录结果
     */
    Map<String, Object> miniAppWechatLogin(String code);

    /**
     * 绑定手机号
     *
     * @param openid 微信openid
     * @param phone 手机号
     * @param code 验证码
     * @return 绑定结果
     */
    Map<String, Object> bindPhone(String openid, String phone, String code);

    /**
     * 小程序退出登录
     *
     * @return 退出结果
     */
    Boolean miniAppLogout();
} 