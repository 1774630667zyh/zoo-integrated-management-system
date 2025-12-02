package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.common.StatusCode;
import com.zoo.zims.entity.Animal;
import com.zoo.zims.mapper.AnimalMapper;
import com.zoo.zims.service.AnimalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 动物管理业务逻辑实现类
 */
@Service
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal> implements AnimalService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerAnimal(Animal animal) {
        // 1. 校验芯片代码唯一性 (虽然数据库有唯一索引，但在业务层校验更友好)
        if (animal.getChipCode() != null) {
            long count = this.count(new LambdaQueryWrapper<Animal>()
                    .eq(Animal::getChipCode, animal.getChipCode()));
            if (count > 0) {
                throw new RuntimeException("芯片代码 " + animal.getChipCode() + " 已存在");
            }
        }

        // 2. 设置默认状态 (如果在园)
        if (animal.getStatus() == null) {
            animal.setStatus(1); // 默认在园
        }

        // 3. 补全时间信息 (如果MyBatis Plus没有配置自动填充Handler，这里手动设置)
        if (animal.getCreateTime() == null) {
            animal.setCreateTime(LocalDateTime.now());
            animal.setUpdateTime(LocalDateTime.now());
        }

        return this.save(animal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferAnimal(Long animalId, Integer targetEnclosureId) {
        Animal animal = this.getById(animalId);
        if (animal == null) {
            throw new RuntimeException("未找到ID为 " + animalId + " 的动物");
        }

        animal.setEnclosureId(targetEnclosureId);
        animal.setUpdateTime(LocalDateTime.now());
        return this.updateById(animal);
    }
}