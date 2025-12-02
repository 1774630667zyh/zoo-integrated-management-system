package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.Food;
import com.zoo.zims.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 饲料库存管理
 */
@RestController
@RequestMapping("/api/foods")
@CrossOrigin
public class FoodController {

    @Autowired
    private FoodService foodService;

    // 分页查询库存
    @GetMapping
    public Result<Page<Food>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(foodService.page(new Page<>(page, size)));
    }

    // 获取所有饲料（用于下拉框）
    @GetMapping("/list")
    public Result<List<Food>> listAll() {
        return Result.success(foodService.list());
    }

    // 新增/入库
    @PostMapping
    public Result<Void> add(@RequestBody Food food) {
        foodService.save(food);
        return Result.success();
    }

    // 修改库存
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody Food food) {
        food.setId(id);
        foodService.updateById(food);
        return Result.success();
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        foodService.removeById(id);
        return Result.success();
    }
}