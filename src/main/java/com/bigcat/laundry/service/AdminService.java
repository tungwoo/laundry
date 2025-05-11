package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.Admin;

/**
 * 管理员Service接口
 */
public interface AdminService extends IService<Admin> {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param ip       登录IP
     * @return 管理员信息
     */
    Admin login(String username, String password, String ip);

    /**
     * 分页查询管理员列表
     *
     * @param page     分页参数
     * @param username 用户名
     * @param name     姓名
     * @param phone    手机号
     * @return 管理员分页列表
     */
    Page<Admin> page(Page<Admin> page, String username, String name, String phone);

    /**
     * 修改密码
     *
     * @param id          管理员ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 更新管理员状态
     *
     * @param id     管理员ID
     * @param status 状态（0-禁用，1-启用）
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);
} 