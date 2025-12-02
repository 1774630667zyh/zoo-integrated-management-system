package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.entity.Species;
import com.zoo.zims.mapper.SpeciesMapper;
import com.zoo.zims.service.SpeciesService;
import org.springframework.stereotype.Service;

@Service
public class SpeciesServiceImpl extends ServiceImpl<SpeciesMapper, Species> implements SpeciesService {
}