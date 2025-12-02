package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.entity.Facility;
import com.zoo.zims.entity.Maintenance;
import com.zoo.zims.mapper.FacilityMapper;
import com.zoo.zims.mapper.MaintenanceMapper;
import com.zoo.zims.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MaintenanceServiceImpl extends ServiceImpl<MaintenanceMapper, Maintenance> implements MaintenanceService {

    @Autowired
    private FacilityMapper facilityMapper;

    @Override
    @Transactional
    public boolean reportMaintenance(Maintenance maintenance) {
        // 1. 初始化状态
        maintenance.setStatus(0); // 待处理
        maintenance.setCreateTime(LocalDateTime.now());

        // 2. 自动将场馆状态置为“维护中”
        Facility facility = facilityMapper.selectById(maintenance.getFacilityId());
        if (facility != null) {
            facility.setStatus(2); // 维护中
            facilityMapper.updateById(facility);
        }

        return this.save(maintenance);
    }

    @Override
    @Transactional
    public boolean completeMaintenance(Long id, String result) {
        Maintenance maintenance = this.getById(id);
        if (maintenance == null) return false;

        // 1. 更新工单状态
        maintenance.setStatus(2); // 已完成
        maintenance.setResult(result);
        maintenance.setResolveTime(LocalDateTime.now());
        this.updateById(maintenance);

        // 2. 恢复场馆状态为“开放”
        Facility facility = facilityMapper.selectById(maintenance.getFacilityId());
        if (facility != null) {
            facility.setStatus(1); // 开放
            facilityMapper.updateById(facility);
        }

        return true;
    }
}