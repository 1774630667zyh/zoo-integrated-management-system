package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.TicketOrder;
import com.zoo.zims.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 票务管理控制层
 * API路径: /api/tickets
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * 游客下单
     */
    @PostMapping("/order")
    public Result<String> createOrder(@RequestBody TicketOrder order) {
        try {
            String orderNo = ticketService.createOrder(order);
            return Result.success(orderNo);
        } catch (Exception e) {
            return Result.error("下单失败: " + e.getMessage());
        }
    }

    /**
     * 查询我的订单 (根据手机号)
     */
    @GetMapping("/my-orders")
    public Result<Page<TicketOrder>> myOrders(
            @RequestParam String mobile,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<TicketOrder> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<TicketOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TicketOrder::getMobile, mobile);
        wrapper.orderByDesc(TicketOrder::getCreateTime);

        return Result.success(ticketService.page(pageParam, wrapper));
    }

    /**
     * 管理员查询所有订单
     */
    @GetMapping("/list")
    public Result<Page<TicketOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo) {

        Page<TicketOrder> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<TicketOrder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(orderNo)) {
            wrapper.eq(TicketOrder::getOrderNo, orderNo);
        }
        wrapper.orderByDesc(TicketOrder::getCreateTime);

        return Result.success(ticketService.page(pageParam, wrapper));
    }

    /**
     * 模拟支付成功回调
     */
    @PutMapping("/pay/{orderNo}")
    public Result<Void> mockPay(@PathVariable String orderNo) {
        TicketOrder order = ticketService.getById(orderNo);
        if (order != null) {
            order.setPayStatus(1); // 已支付
            ticketService.updateById(order);
            return Result.success();
        }
        return Result.error("订单不存在");
    }
}