package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.FeedingPlan;
import com.zoo.zims.service.FeedingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 喂养计划管理
 */
@RestController
@RequestMapping("/api/feeding-plans")
@CrossOrigin
public class FeedingPlanController {

    @Autowired
    private FeedingPlanService feedingPlanService;

    // 获取某只动物的喂养计划
    @GetMapping("/animal/{animalId}")
    public Result<List<FeedingPlan>> getByAnimal(@PathVariable Long animalId) {
        LambdaQueryWrapper<FeedingPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeedingPlan::getAnimalId, animalId);
        return Result.success(feedingPlanService.list(wrapper));
    }

    // 新增计划
    @PostMapping
    public Result<Void> add(@RequestBody FeedingPlan plan) {
        feedingPlanService.save(plan);
        return Result.success();
    }

    // 删除计划
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        feedingPlanService.removeById(id);
        return Result.success();
    }
}