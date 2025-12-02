package com.zoo.zims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoo.zims.entity.Species;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpeciesMapper extends BaseMapper<Species> {
}