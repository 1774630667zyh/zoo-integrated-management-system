package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.common.StatusCode;
import com.zoo.zims.entity.Animal;
import com.zoo.zims.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 动物管理控制层
 * API路径: /api/animals
 */
@RestController
@RequestMapping("/api/animals")
@CrossOrigin // 允许跨域请求
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    /**
     * 分页查询动物列表
     * 支持按名称模糊搜索
     * * @param page 当前页码
     * @param size 每页大小
     * @param name 动物名称(可选)
     * @return 分页结果
     */
    @GetMapping
    public Result<Page<Animal>> findPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {

        Page<Animal> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();

        // 如果提供了名称，则进行模糊查询
        if (StringUtils.hasText(name)) {
            wrapper.like(Animal::getName, name);
        }

        // 按更新时间倒序排列
        wrapper.orderByDesc(Animal::getUpdateTime);

        Page<Animal> resultPage = animalService.page(pageParam, wrapper);
        return Result.success(resultPage);
    }

    /**
     * 根据ID查询动物详情
     * @param id 动物ID
     */
    @GetMapping("/{id}")
    public Result<Animal> findById(@PathVariable Long id) {
        Animal animal = animalService.getById(id);
        if (animal == null) {
            return new Result<>(false, StatusCode.ANIMAL_NOT_FOUND, "未找到该动物信息");
        }
        return Result.success(animal);
    }

    /**
     * 新增动物 (入园登记)
     * @param animal 动物实体
     */
    @PostMapping
    public Result<Void> add(@RequestBody Animal animal) {
        try {
            animalService.registerAnimal(animal);
            return Result.success();
        } catch (Exception e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    /**
     * 修改动物信息
     * @param id 动物ID
     * @param animal 修改后的信息
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Animal animal) {
        animal.setId(id);
        boolean success = animalService.updateById(animal);
        if (success) {
            return Result.success();
        } else {
            return Result.error("修改失败，可能ID不存在");
        }
    }

    /**
     * 删除动物 (逻辑删除或物理删除，视具体业务而定，此处演示物理删除)
     * @param id 动物ID
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = animalService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 场馆调拨接口
     * @param id 动物ID
     * @param enclosureId 目标场馆ID
     */
    @PutMapping("/{id}/transfer/{enclosureId}")
    public Result<Void> transfer(@PathVariable Long id, @PathVariable Integer enclosureId) {
        try {
            animalService.transferAnimal(id, enclosureId);
            return Result.success();
        } catch (Exception e) {
            return Result.error("调拨失败: " + e.getMessage());
        }
    }
}