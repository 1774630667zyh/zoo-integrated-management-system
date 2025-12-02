package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.Maintenance;
import com.zoo.zims.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    // 分页查询工单
    @GetMapping
    public Result<Page<Maintenance>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer facilityId) {

        Page<Maintenance> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Maintenance> wrapper = new LambdaQueryWrapper<>();
        if (facilityId != null) {
            wrapper.eq(Maintenance::getFacilityId, facilityId);
        }
        wrapper.orderByDesc(Maintenance::getCreateTime);

        return Result.success(maintenanceService.page(pageParam, wrapper));
    }

    // 提交报修
    @PostMapping
    public Result<Void> report(@RequestBody Maintenance maintenance) {
        maintenanceService.reportMaintenance(maintenance);
        return Result.success();
    }

    // 维修完成
    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id, @RequestBody Maintenance maintenance) {
        maintenanceService.completeMaintenance(id, maintenance.getResult());
        return Result.success();
    }
}