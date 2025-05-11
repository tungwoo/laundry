package com.bigcat.laundry.controller.miniapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.ClothesType;
import com.bigcat.laundry.entity.Order;
import com.bigcat.laundry.service.ClothesTypeService;
import com.bigcat.laundry.service.OrderService;
import com.bigcat.laundry.vo.OrderCreateVO;
import com.bigcat.laundry.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 微信小程序订单控制器
 */
@Api(tags = "小程序-订单管理")
@RestController
@RequestMapping("/miniapp/order")
@RequiredArgsConstructor
public class MiniappOrderController {

    private final OrderService orderService;
    private final ClothesTypeService clothesTypeService;

    /**
     * 分页查询会员订单列表
     */
    @ApiOperation("分页查询会员订单列表")
    @PostMapping("/page")
    public Result<Page<Order>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页记录数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("会员ID") @RequestParam Long memberId,
            @ApiParam("订单状态") @RequestParam(required = false) Integer status) {
        Page<Order> result = orderService.page(
                new Page<>(page, pageSize), null, null, status, null, null);
        return Result.success(result);
    }

    /**
     * 创建订单
     */
    @ApiOperation("创建订单")
    @PostMapping
    public Result<OrderVO> createOrder(@RequestBody @Valid OrderCreateVO orderCreateVO) {
        OrderVO orderVO = orderService.createOrder(orderCreateVO);
        return Result.success(orderVO);
    }

    /**
     * 根据订单编号查询订单详情
     */
    @ApiOperation("根据订单编号查询订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderVO> getOrderDetail(@PathVariable String orderNo) {
        OrderVO orderVO = orderService.getOrderDetail(orderNo);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancel/{orderNo}")
    public Result<Boolean> cancelOrder(
            @PathVariable String orderNo,
            @ApiParam("会员ID") @RequestParam Long memberId) {
        boolean success = orderService.cancelOrder(orderNo, memberId, "会员取消");
        return Result.success(success);
    }

    /**
     * 支付订单
     */
    @ApiOperation("支付订单")
    @PostMapping("/pay")
    public Result<Boolean> payOrder(
            @ApiParam("订单编号") @RequestParam String orderNo,
            @ApiParam("支付方式") @RequestParam Integer payType,
            @ApiParam("会员ID") @RequestParam Long memberId) {
        boolean success = orderService.payOrder(orderNo, payType, memberId, "会员支付");
        return Result.success(success);
    }

    /**
     * 查询所有衣物类型
     */
    @ApiOperation("查询所有衣物类型")
    @GetMapping("/clothes-type/list")
    public Result<List<ClothesType>> listClothesTypes() {
        List<ClothesType> list = clothesTypeService.listEnabled();
        return Result.success(list);
    }
} 