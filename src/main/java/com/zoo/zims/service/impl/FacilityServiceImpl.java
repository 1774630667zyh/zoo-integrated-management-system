package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.entity.Facility;
import com.zoo.zims.mapper.FacilityMapper;
import com.zoo.zims.service.FacilityService;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl extends ServiceImpl<FacilityMapper, Facility> implements FacilityService {
}