package com.bigcat.laundry.controller.admin;

import com.bigcat.laundry.common.result.ResultModel;
import com.bigcat.laundry.service.AuthService;
import com.bigcat.laundry.vo.request.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理后台认证控制器
 */
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录
     *
     * @param loginVO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResultModel<Map<String, Object>> login(@RequestBody LoginVO loginVO) {
        Map<String, Object> result = authService.adminLogin(loginVO.getUsername(), loginVO.getPassword());
        return ResultModel.ok(result);
    }

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @PostMapping("/logout")
    public ResultModel<Boolean> logout() {
        return ResultModel.ok(authService.adminLogout());
    }
} 