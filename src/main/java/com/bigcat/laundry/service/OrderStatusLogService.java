package com.bigcat.laundry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigcat.laundry.entity.OrderStatusLog;

import java.util.List;

/**
 * 订单状态日志Service接口
 */
public interface OrderStatusLogService extends IService<OrderStatusLog> {

    /**
     * 记录订单状态变更日志
     *
     * @param orderId       订单ID
     * @param orderNo       订单编号
     * @param beforeStatus  之前状态
     * @param afterStatus   之后状态
     * @param description   操作描述
     * @param operatorId    操作人ID
     * @param operatorName  操作人姓名
     * @param remark        备注
     * @return 是否成功
     */
    boolean addLog(Long orderId, String orderNo, Integer beforeStatus, Integer afterStatus,
                  String description, Long operatorId, String operatorName, String remark);

    /**
     * 查询订单状态日志列表
     *
     * @param orderNo 订单编号
     * @return 订单状态日志列表
     */
    List<OrderStatusLog> listByOrderNo(String orderNo);

    /**
     * 分页查询订单状态日志
     *
     * @param page    分页参数
     * @param orderNo 订单编号
     * @return 订单状态日志分页列表
     */
    Page<OrderStatusLog> page(Page<OrderStatusLog> page, String orderNo);
} 