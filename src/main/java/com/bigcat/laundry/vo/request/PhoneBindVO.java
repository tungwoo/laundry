package com.bigcat.laundry.vo.request;

import lombok.Data;

/**
 * 手机号绑定请求VO
 */
@Data
public class PhoneBindVO {

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
} 