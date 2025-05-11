package com.bigcat.laundry.controller.admin;

import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.Admin;
import com.bigcat.laundry.service.AdminService;
import com.bigcat.laundry.vo.AdminLoginVO;
import com.bigcat.laundry.vo.AdminUpdatePasswordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理后台登录控制器
 */
@Api(tags = "管理后台-登录管理")
@RestController
@RequestMapping("/admin/login")
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminService adminService;

    /**
     * 管理员登录
     */
    @ApiOperation("管理员登录")
    @PostMapping
    public Result<Admin> login(@RequestBody @Valid AdminLoginVO loginVO, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Admin admin = adminService.login(loginVO.getUsername(), loginVO.getPassword(), ip);
        return Result.success(admin);
    }

    /**
     * 修改密码
     */
    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public Result<Boolean> updatePassword(@RequestBody @Valid AdminUpdatePasswordVO updatePasswordVO) {
        boolean success = adminService.updatePassword(
                updatePasswordVO.getId(), 
                updatePasswordVO.getOldPassword(), 
                updatePasswordVO.getNewPassword());
        return Result.success(success);
    }
} 