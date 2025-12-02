package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.entity.Animal;
import com.zoo.zims.entity.MedicalRecord;
import com.zoo.zims.mapper.AnimalMapper;
import com.zoo.zims.mapper.MedicalRecordMapper;
import com.zoo.zims.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MedicalServiceImpl extends ServiceImpl<MedicalRecordMapper, MedicalRecord> implements MedicalService {

    @Autowired
    private AnimalMapper animalMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRecord(MedicalRecord record) {
        // 1. 校验动物是否存在
        Animal animal = animalMapper.selectById(record.getAnimalId());
        if (animal == null) {
            throw new RuntimeException("关联动物不存在，ID: " + record.getAnimalId());
        }

        // 2. 补全就诊时间 (如果未填写)
        if (record.getVisitDate() == null) {
            record.setVisitDate(LocalDateTime.now());
        }

        // 3. 业务逻辑扩展：如果诊断结果包含"隔离"字样，可以自动更新动物状态为隔离中
        // if (record.getDiagnosis().contains("传染")) { ... }

        return this.save(record);
    }
}