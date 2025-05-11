package com.bigcat.laundry.vo.request;

import lombok.Data;

/**
 * 微信登录请求VO
 */
@Data
public class WechatLoginVO {

    /**
     * 微信临时登录凭证
     */
    private String code;
} 