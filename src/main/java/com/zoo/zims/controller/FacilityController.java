package com.zoo.zims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoo.zims.common.Result;
import com.zoo.zims.entity.Facility;
import com.zoo.zims.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@CrossOrigin
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping
    public Result<Page<Facility>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(facilityService.page(new Page<>(page, size)));
    }

    @GetMapping("/list")
    public Result<List<Facility>> listAll() {
        return Result.success(facilityService.list());
    }

    @PostMapping
    public Result<Void> add(@RequestBody Facility facility) {
        facilityService.save(facility);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody Facility facility) {
        facility.setId(id);
        facilityService.updateById(facility);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        facilityService.removeById(id);
        return Result.success();
    }
}