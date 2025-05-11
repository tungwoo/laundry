package com.bigcat.laundry.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.Order;
import com.bigcat.laundry.entity.OrderStatusLog;
import com.bigcat.laundry.service.OrderService;
import com.bigcat.laundry.service.OrderStatusLogService;
import com.bigcat.laundry.vo.OrderCreateVO;
import com.bigcat.laundry.vo.OrderDeliveryVO;
import com.bigcat.laundry.vo.OrderShelfVO;
import com.bigcat.laundry.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理后台订单管理控制器
 */
@Api(tags = "管理后台-订单管理")
@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderStatusLogService orderStatusLogService;

    /**
     * 分页查询订单列表
     */
    @ApiOperation("分页查询订单列表")
    @PostMapping("/page")
    public Result<Page<Order>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页记录数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("订单编号") @RequestParam(required = false) String orderNo,
            @ApiParam("会员手机号") @RequestParam(required = false) String memberPhone,
            @ApiParam("订单状态") @RequestParam(required = false) Integer status,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<Order> result = orderService.page(
                new Page<>(page, pageSize), orderNo, memberPhone, status, startTime, endTime);
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
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("操作人姓名") @RequestParam String operatorName) {
        boolean success = orderService.cancelOrder(orderNo, operatorId, operatorName);
        return Result.success(success);
    }

    /**
     * 更新订单状态
     */
    @ApiOperation("更新订单状态")
    @PostMapping("/status")
    public Result<Boolean> updateOrderStatus(
            @ApiParam("订单编号") @RequestParam String orderNo,
            @ApiParam("状态") @RequestParam Integer status,
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("操作人姓名") @RequestParam String operatorName) {
        boolean success = orderService.updateOrderStatus(orderNo, status, operatorId, operatorName);
        return Result.success(success);
    }

    /**
     * 上架订单
     */
    @ApiOperation("上架订单")
    @PostMapping("/shelf")
    public Result<Boolean> shelfOrder(@RequestBody @Valid OrderShelfVO shelfVO) {
        boolean success = orderService.shelfOrder(
                shelfVO.getOrderNo(),
                shelfVO.getShelfLocation(),
                shelfVO.getShelfNumber(),
                shelfVO.getOperatorId(),
                shelfVO.getOperatorName());
        return Result.success(success);
    }

    /**
     * 顾客自取订单
     */
    @ApiOperation("顾客自取订单")
    @PostMapping("/pickup")
    public Result<Boolean> pickupOrder(
            @ApiParam("订单编号") @RequestParam String orderNo,
            @ApiParam("取衣码") @RequestParam String pickupCode,
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("操作人姓名") @RequestParam String operatorName) {
        boolean success = orderService.pickupOrder(orderNo, pickupCode, operatorId, operatorName);
        return Result.success(success);
    }

    /**
     * 配送订单
     */
    @ApiOperation("配送订单")
    @PostMapping("/delivery")
    public Result<Boolean> deliveryOrder(@RequestBody @Valid OrderDeliveryVO deliveryVO) {
        boolean success = orderService.deliveryOrder(
                deliveryVO.getOrderNo(),
                deliveryVO.getDeliveryAddress(),
                deliveryVO.getDeliveryPhoto(),
                deliveryVO.getOperatorId(),
                deliveryVO.getOperatorName());
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
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("操作人姓名") @RequestParam String operatorName) {
        boolean success = orderService.payOrder(orderNo, payType, operatorId, operatorName);
        return Result.success(success);
    }

    /**
     * 退款订单
     */
    @ApiOperation("退款订单")
    @PostMapping("/refund/{orderNo}")
    public Result<Boolean> refundOrder(
            @PathVariable String orderNo,
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("操作人姓名") @RequestParam String operatorName) {
        boolean success = orderService.refundOrder(orderNo, operatorId, operatorName);
        return Result.success(success);
    }

    /**
     * 查询订单状态日志
     */
    @ApiOperation("查询订单状态日志")
    @GetMapping("/status-log/{orderNo}")
    public Result<List<OrderStatusLog>> getOrderStatusLogs(@PathVariable String orderNo) {
        List<OrderStatusLog> logs = orderStatusLogService.listByOrderNo(orderNo);
        return Result.success(logs);
    }
} 