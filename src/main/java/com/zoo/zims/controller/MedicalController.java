package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.MedicalRecord;
import com.zoo.zims.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医疗记录控制层
 * API路径: /api/medical
 */
@RestController
@RequestMapping("/api/medical")
@CrossOrigin
public class MedicalController {

    @Autowired
    private MedicalService medicalService;

    /**
     * 获取某只动物的病历历史
     * @param animalId 动物ID
     */
    @GetMapping("/history/{animalId}")
    public Result<Page<MedicalRecord>> getHistoryByAnimal(
            @PathVariable Long animalId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<MedicalRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicalRecord::getAnimalId, animalId);
        wrapper.orderByDesc(MedicalRecord::getVisitDate); // 按就诊时间倒序

        return Result.success(medicalService.page(pageParam, wrapper));
    }

    /**
     * 录入新病历 (兽医操作)
     */
    @PostMapping
    public Result<Void> addRecord(@RequestBody MedicalRecord record) {
        try {
            medicalService.createRecord(record);
            return Result.success();
        } catch (Exception e) {
            return Result.error("录入失败: " + e.getMessage());
        }
    }

    /**
     * 获取病历详情
     */
    @GetMapping("/{id}")
    public Result<MedicalRecord> getDetail(@PathVariable Long id) {
        return Result.success(medicalService.getById(id));
    }
}