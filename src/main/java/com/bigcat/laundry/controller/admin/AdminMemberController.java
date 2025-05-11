package com.bigcat.laundry.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.Member;
import com.bigcat.laundry.entity.MemberTransaction;
import com.bigcat.laundry.service.MemberService;
import com.bigcat.laundry.service.MemberTransactionService;
import com.bigcat.laundry.vo.MemberRechargeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 管理后台会员管理控制器
 */
@Api(tags = "管理后台-会员管理")
@RestController
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;
    private final MemberTransactionService memberTransactionService;

    /**
     * 分页查询会员列表
     */
    @ApiOperation("分页查询会员列表")
    @PostMapping("/page")
    public Result<Page<Member>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页记录数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("会员姓名") @RequestParam(required = false) String name,
            @ApiParam("手机号") @RequestParam(required = false) String phone) {
        Page<Member> result = memberService.page(new Page<>(page, pageSize), name, phone);
        return Result.success(result);
    }

    /**
     * 根据ID查询会员
     */
    @ApiOperation("根据ID查询会员")
    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        return Result.success(member);
    }

    /**
     * 新增会员
     */
    @ApiOperation("新增会员")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid Member member) {
        boolean success = memberService.save(member);
        return Result.success(success);
    }

    /**
     * 修改会员
     */
    @ApiOperation("修改会员")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid Member member) {
        boolean success = memberService.updateById(member);
        return Result.success(success);
    }

    /**
     * 删除会员
     */
    @ApiOperation("删除会员")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        Member member = memberService.getById(id);
        member.setDeleted(1);
        boolean success = memberService.updateById(member);
        return Result.success(success);
    }

    /**
     * 会员充值
     */
    @ApiOperation("会员充值")
    @PostMapping("/recharge")
    public Result<Boolean> recharge(@RequestBody @Valid MemberRechargeVO rechargeVO) {
        boolean success;
        // 判断是否有赠送金额
        if (rechargeVO.getGiftAmount() != null && rechargeVO.getGiftAmount().compareTo(BigDecimal.ZERO) > 0) {
            success = memberService.rechargeWithGift(
                    rechargeVO.getId(),
                    rechargeVO.getAmount(),
                    rechargeVO.getGiftAmount(),
                    rechargeVO.getOperatorId(),
                    rechargeVO.getOperatorName());
        } else {
            success = memberService.recharge(
                    rechargeVO.getId(),
                    rechargeVO.getAmount(),
                    rechargeVO.getOperatorId(),
                    rechargeVO.getOperatorName());
        }
        return Result.success(success);
    }

    /**
     * 分页查询会员交易流水
     */
    @ApiOperation("分页查询会员交易流水")
    @PostMapping("/transaction/page")
    public Result<Page<MemberTransaction>> transactionPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页记录数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("会员ID") @RequestParam(required = false) Long memberId,
            @ApiParam("交易类型") @RequestParam(required = false) Integer type) {
        Page<MemberTransaction> result = memberTransactionService.page(
                new Page<>(page, pageSize), memberId, type);
        return Result.success(result);
    }
} 