package com.bigcat.laundry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigcat.laundry.common.exception.BusinessException;
import com.bigcat.laundry.entity.Admin;
import com.bigcat.laundry.mapper.AdminMapper;
import com.bigcat.laundry.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 管理员Service实现类
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public Admin login(String username, String password, String ip) {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username)
                .eq(Admin::getDeleted, 0));
        
        if (admin == null) {
            throw new BusinessException("账号不存在");
        }
        
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 密码加密比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(admin.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 更新登录信息
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(ip);
        updateById(admin);
        
        // 清空密码
        admin.setPassword(null);
        
        return admin;
    }

    @Override
    public Page<Admin> page(Page<Admin> page, String username, String name, String phone) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<Admin>()
                .eq(Admin::getDeleted, 0)
                .like(StringUtils.hasText(username), Admin::getUsername, username)
                .like(StringUtils.hasText(name), Admin::getName, name)
                .like(StringUtils.hasText(phone), Admin::getPhone, phone)
                .orderByDesc(Admin::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        Admin admin = getById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        
        // 验证旧密码
        String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!encryptedOldPassword.equals(admin.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        
        // 更新密码
        admin.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        admin.setUpdateTime(LocalDateTime.now());
        return updateById(admin);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (!Objects.equals(status, 0) && !Objects.equals(status, 1)) {
            throw new BusinessException("状态值不正确");
        }
        
        Admin admin = getById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        
        // 超级管理员不能被禁用
        if (admin.getRole() == 1 && status == 0) {
            throw new BusinessException("超级管理员不能被禁用");
        }
        
        admin.setStatus(status);
        admin.setUpdateTime(LocalDateTime.now());
        return updateById(admin);
    }
} 