package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zoo.zims.common.Result;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.entity.*;
import com.zoo.zims.mapper.CommentMapper;
import com.zoo.zims.mapper.TicketProductMapper;
import com.zoo.zims.service.AnimalService;
import com.zoo.zims.service.FacilityService;
import com.zoo.zims.service.TicketService;
import com.zoo.zims.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 游客端控制器
 */
@RestController
@RequestMapping("/api/visitor")
@CrossOrigin
public class VisitorController {

    @Autowired
    private TicketProductMapper ticketProductMapper;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private AnimalService animalService;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private CommentMapper commentMapper;

    // --- 1. 账号功能 ---

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(visitorService.login(loginDTO));
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody Visitor visitor) {
        visitorService.register(visitor);
        return Result.success();
    }

    // --- 2. 基础业务 ---

    // 获取在售票种列表
    @GetMapping("/products")
    public Result<List<TicketProduct>> getProducts() {
        return Result.success(ticketProductMapper.selectList(
                new LambdaQueryWrapper<TicketProduct>().eq(TicketProduct::getStatus, 1)
        ));
    }

    // 游客下单 (支持登录和未登录)
    @PostMapping("/order")
    public Result<String> createOrder(@RequestBody TicketOrder order, HttpServletRequest request) {
        try {
            // 尝试从Request获取用户ID (如果已登录，拦截器会放入)
            Object userIdObj = request.getAttribute("userId");
            if (userIdObj != null) {
                order.setUserId(Long.parseLong(userIdObj.toString()));
            } else {
                order.setUserId(null); // 未登录下单
            }

            String orderNo = ticketService.createOrder(order);
            return Result.success(orderNo);
        } catch (Exception e) {
            return Result.error("下单失败: " + e.getMessage());
        }
    }

    // 游客查询订单 (通过手机号)
    @GetMapping("/orders")
    public Result<List<TicketOrder>> getOrders(@RequestParam String mobile) {
        return Result.success(ticketService.list(
                new LambdaQueryWrapper<TicketOrder>()
                        .eq(TicketOrder::getMobile, mobile)
                        .orderByDesc(TicketOrder::getCreateTime)
        ));
    }

    // 游客查看动物
    @GetMapping("/animals")
    public Result<List<Animal>> getAnimals() {
        return Result.success(animalService.list(
                new LambdaQueryWrapper<Animal>().eq(Animal::getStatus, 1)
        ));
    }

    // 游客查看场馆
    @GetMapping("/facilities")
    public Result<List<Facility>> getFacilities() {
        return Result.success(facilityService.list(
                new LambdaQueryWrapper<Facility>().eq(Facility::getStatus, 1)
        ));
    }

    // --- 3. 社区互动 (评论) ---

    // 获取评论列表 (公开)
    @GetMapping("/comments/list/{type}/{targetId}")
    public Result<List<Comment>> getComments(@PathVariable String type, @PathVariable Long targetId) {
        return Result.success(commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getType, type)
                        .eq(Comment::getTargetId, targetId)
                        .orderByDesc(Comment::getCreateTime)
        ));
    }

    // 发布评论 (需登录)
    @PostMapping("/comment")
    public Result<Void> addComment(@RequestBody Comment comment, HttpServletRequest request) {
        Object userIdObj = request.getAttribute("userId");
        if (userIdObj == null) {
            return Result.error("请先登录");
        }

        comment.setVisitorId(Long.parseLong(userIdObj.toString()));
        // 简单处理：实际应根据ID查最新的昵称，这里简化处理
        if (comment.getNickname() == null) {
            comment.setNickname("游客" + userIdObj);
        }

        commentMapper.insert(comment);
        return Result.success();
    }
}