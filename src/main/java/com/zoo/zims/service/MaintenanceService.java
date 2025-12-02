package com.zoo.zims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoo.zims.entity.Maintenance;

public interface MaintenanceService extends IService<Maintenance> {

    /**
     * 提交报修
     */
    boolean reportMaintenance(Maintenance maintenance);

    /**
     * 完成维修
     */
    boolean completeMaintenance(Long id, String result);
}