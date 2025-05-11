package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 微信登录VO类
 */
@Data
public class WxLoginVO {

    /**
     * 微信code
     */
    @NotEmpty(message = "code不能为空")
    private String code;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 头像url
     */
    private String avatarUrl;
} 