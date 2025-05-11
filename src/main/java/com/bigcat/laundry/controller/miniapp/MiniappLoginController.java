package com.bigcat.laundry.controller.miniapp;

import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.Member;
import com.bigcat.laundry.service.MemberService;
import com.bigcat.laundry.vo.WxLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 微信小程序登录控制器
 */
@Api(tags = "小程序-登录管理")
@RestController
@RequestMapping("/miniapp/login")
@RequiredArgsConstructor
public class MiniappLoginController {

    private final MemberService memberService;

    /**
     * 微信登录
     */
    @ApiOperation("微信登录")
    @PostMapping("/wx")
    public Result<Member> wxLogin(@RequestBody @Valid WxLoginVO wxLoginVO) {
        // 这里简化处理，实际应该调用微信API获取openid
        String openid = wxLoginVO.getCode(); // 简化处理，直接把code当作openid
        
        // 查询会员信息
        Member member = memberService.getByOpenid(openid);
        
        // 如果会员不存在，创建新会员
        if (member == null) {
            member = new Member();
            member.setOpenid(openid);
            member.setName(wxLoginVO.getNickName());
            member.setPhone("");
            member.setGender(wxLoginVO.getGender());
            member.setBalance(java.math.BigDecimal.ZERO);
            member.setPoints(0);
            member.setLevel(1);
            member.setCreateTime(java.time.LocalDateTime.now());
            member.setUpdateTime(java.time.LocalDateTime.now());
            member.setDeleted(0);
            memberService.save(member);
        }
        
        return Result.success(member);
    }

    /**
     * 更新会员信息
     */
    @ApiOperation("更新会员信息")
    @PutMapping
    public Result<Boolean> updateInfo(@RequestBody @Valid Member member) {
        boolean success = memberService.updateById(member);
        return Result.success(success);
    }
} 