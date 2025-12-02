package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.Species;
import com.zoo.zims.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物种管理控制层
 * 用于前端下拉框数据获取及物种维护
 */
@RestController
@RequestMapping("/api/species")
@CrossOrigin
public class SpeciesController {

    @Autowired
    private SpeciesService speciesService;

    /**
     * 获取所有物种列表 (用于下拉框选择)
     */
    @GetMapping("/list")
    public Result<List<Species>> listAll() {
        return Result.success(speciesService.list());
    }

    /**
     * 新增物种
     */
    @PostMapping
    public Result<Void> add(@RequestBody Species species) {
        // 简单校验学名是否重复
        long count = speciesService.count(new LambdaQueryWrapper<Species>()
                .eq(Species::getScientificName, species.getScientificName()));
        if (count > 0) {
            return Result.error("该学名已存在");
        }
        speciesService.save(species);
        return Result.success();
    }
}