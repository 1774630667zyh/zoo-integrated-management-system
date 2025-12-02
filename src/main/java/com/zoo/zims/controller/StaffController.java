package com.zoo.zims.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.Staff;
import com.zoo.zims.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理 (仅管理员可用)
 */
@RestController
@RequestMapping("/api/staffs")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public Result<Page<Staff>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(staffService.page(new Page<>(page, size)));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Staff staff) {
        try {
            staffService.createStaff(staff);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Staff staff) {
        staff.setId(id);
        // 不允许直接通过此接口修改密码
        staff.setPassword(null);
        staffService.updateById(staff);
        return Result.success();
    }
}