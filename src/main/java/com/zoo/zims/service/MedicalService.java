package com.zoo.zims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoo.zims.entity.MedicalRecord;

public interface MedicalService extends IService<MedicalRecord> {

    /**
     * 创建诊疗记录
     * @param record 记录信息
     * @return 是否成功
     */
    boolean createRecord(MedicalRecord record);
}