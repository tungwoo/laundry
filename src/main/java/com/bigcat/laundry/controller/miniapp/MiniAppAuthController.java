package com.bigcat.laundry.controller.miniapp;

import com.bigcat.laundry.common.result.ResultModel;
import com.bigcat.laundry.service.AuthService;
import com.bigcat.laundry.vo.request.LoginVO;
import com.bigcat.laundry.vo.request.WechatLoginVO;
import com.bigcat.laundry.vo.request.PhoneBindVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 微信小程序认证控制器
 */
@RestController
@RequestMapping("/api/miniapp/auth")
public class MiniAppAuthController {

    @Autowired
    private AuthService authService;

    /**
     * 账号密码登录
     *
     * @param loginVO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResultModel<Map<String, Object>> login(@RequestBody LoginVO loginVO) {
        Map<String, Object> result = authService.miniAppLogin(loginVO.getUsername(), loginVO.getPassword());
        return ResultModel.ok(result);
    }

    /**
     * 微信一键登录
     *
     * @param wechatLoginVO 微信登录参数
     * @return 登录结果
     */
    @PostMapping("/wechat-login")
    public ResultModel<Map<String, Object>> wechatLogin(@RequestBody WechatLoginVO wechatLoginVO) {
        Map<String, Object> result = authService.miniAppWechatLogin(wechatLoginVO.getCode());
        return ResultModel.ok(result);
    }

    /**
     * 绑定手机号
     *
     * @param phoneBindVO 绑定参数
     * @return 绑定结果
     */
    @PostMapping("/bind-phone")
    public ResultModel<Map<String, Object>> bindPhone(@RequestBody PhoneBindVO phoneBindVO) {
        Map<String, Object> result = authService.bindPhone(
            phoneBindVO.getOpenid(),
            phoneBindVO.getPhone(),
            phoneBindVO.getCode()
        );
        return ResultModel.ok(result);
    }

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @PostMapping("/logout")
    public ResultModel<Boolean> logout() {
        return ResultModel.ok(authService.miniAppLogout());
    }
} 