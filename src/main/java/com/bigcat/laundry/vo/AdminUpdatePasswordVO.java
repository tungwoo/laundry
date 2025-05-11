package com.bigcat.laundry.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 管理员修改密码VO类
 */
@Data
public class AdminUpdatePasswordVO {

    /**
     * 管理员ID
     */
    @NotNull(message = "管理员ID不能为空")
    private Long id;

    /**
     * 旧密码
     */
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
} 