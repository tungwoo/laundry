package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 管理员登录VO类
 */
@Data
public class AdminLoginVO {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;
} 