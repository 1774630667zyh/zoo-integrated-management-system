package com.zoo.zims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoo.zims.entity.Animal;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动物数据访问层接口
 * 继承BaseMapper后，无需编写Mapper.xml即可拥有基础CRUD功能
 */
@Mapper
public interface AnimalMapper extends BaseMapper<Animal> {
    // 如果需要复杂的多表联查，可在此处定义方法并在 resources/mapper/AnimalMapper.xml 中编写SQL
}