package com.zoo.zims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoo.zims.entity.Food;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
}