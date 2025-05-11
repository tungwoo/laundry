package com.bigcat.laundry.controller.miniapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.Member;
import com.bigcat.laundry.entity.MemberTransaction;
import com.bigcat.laundry.service.MemberService;
import com.bigcat.laundry.service.MemberTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序会员控制器
 */
@Api(tags = "小程序-会员管理")
@RestController
@RequestMapping("/miniapp/member")
@RequiredArgsConstructor
public class MiniappMemberController {

    private final MemberService memberService;
    private final MemberTransactionService memberTransactionService;

    /**
     * 获取会员信息
     */
    @ApiOperation("获取会员信息")
    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        return Result.success(member);
    }

    /**
     * 绑定手机号
     */
    @ApiOperation("绑定手机号")
    @PostMapping("/bindPhone")
    public Result<Boolean> bindPhone(
            @ApiParam("会员ID") @RequestParam Long id,
            @ApiParam("手机号") @RequestParam String phone) {
        Member member = memberService.getById(id);
        member.setPhone(phone);
        boolean success = memberService.updateById(member);
        return Result.success(success);
    }

    /**
     * 查询会员总余额
     */
    @ApiOperation("查询会员总余额")
    @GetMapping("/balance/{id}")
    public Result<Object> getBalance(@PathVariable Long id) {
        Member member = memberService.getById(id);
        if (member == null) {
            return Result.error("会员不存在");
        }
        
        return Result.success(
            java.util.Map.of(
                "balance", member.getBalance(),
                "giftBalance", member.getGiftBalance(),
                "totalBalance", member.getBalance().add(member.getGiftBalance())
            )
        );
    }

    /**
     * 查询会员交易流水
     */
    @ApiOperation("查询会员交易流水")
    @PostMapping("/transaction/page")
    public Result<Page<MemberTransaction>> transactionPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页记录数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("会员ID") @RequestParam Long memberId,
            @ApiParam("交易类型") @RequestParam(required = false) Integer type) {
        Page<MemberTransaction> result = memberTransactionService.page(
                new Page<>(page, pageSize), memberId, type);
        return Result.success(result);
    }
} 