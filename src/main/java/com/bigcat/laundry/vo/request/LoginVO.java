package com.bigcat.laundry.vo.request;

import lombok.Data;

/**
 * 登录请求VO
 */
@Data
public class LoginVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
} 