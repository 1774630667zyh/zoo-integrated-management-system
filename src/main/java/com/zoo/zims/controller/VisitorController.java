package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.Animal;
import com.zoo.zims.entity.Facility;
import com.zoo.zims.entity.TicketOrder;
import com.zoo.zims.entity.TicketProduct;
import com.zoo.zims.mapper.TicketProductMapper;
import com.zoo.zims.service.AnimalService;
import com.zoo.zims.service.FacilityService;
import com.zoo.zims.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 游客端控制器
 * 无需登录即可访问
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

    // 1. 获取在售票种列表
    @GetMapping("/products")
    public Result<List<TicketProduct>> getProducts() {
        return Result.success(ticketProductMapper.selectList(
                new LambdaQueryWrapper<TicketProduct>().eq(TicketProduct::getStatus, 1)
        ));
    }

    // 2. 游客下单 (无需Token)
    @PostMapping("/order")
    public Result<String> createOrder(@RequestBody TicketOrder order) {
        try {
            // 游客下单 userId 为空
            order.setUserId(null);
            String orderNo = ticketService.createOrder(order);
            return Result.success(orderNo);
        } catch (Exception e) {
            return Result.error("下单失败: " + e.getMessage());
        }
    }

    // 3. 游客查询订单 (通过手机号)
    @GetMapping("/orders")
    public Result<List<TicketOrder>> getOrders(@RequestParam String mobile) {
        return Result.success(ticketService.list(
                new LambdaQueryWrapper<TicketOrder>()
                        .eq(TicketOrder::getMobile, mobile)
                        .orderByDesc(TicketOrder::getCreateTime)
        ));
    }

    // 4. 游客查看动物风采 (只看在园的)
    @GetMapping("/animals")
    public Result<List<Animal>> getAnimals() {
        return Result.success(animalService.list(
                new LambdaQueryWrapper<Animal>().eq(Animal::getStatus, 1)
        ));
    }

    // 5. 游客查看场馆 (只看开放的)
    @GetMapping("/facilities")
    public Result<List<Facility>> getFacilities() {
        return Result.success(facilityService.list(
                new LambdaQueryWrapper<Facility>().eq(Facility::getStatus, 1)
        ));
    }
}