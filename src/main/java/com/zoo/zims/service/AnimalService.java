package com.zoo.zims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoo.zims.entity.Animal;

/**
 * 动物管理业务逻辑接口
 */
public interface AnimalService extends IService<Animal> {

    /**
     * 注册新动物入园
     * @param animal 动物信息
     * @return 注册是否成功
     */
    boolean registerAnimal(Animal animal);

    /**
     * 动物转运/调拨场馆
     * @param animalId 动物ID
     * @param targetEnclosureId 目标场馆ID
     * @return 是否成功
     */
    boolean transferAnimal(Long animalId, Integer targetEnclosureId);
}