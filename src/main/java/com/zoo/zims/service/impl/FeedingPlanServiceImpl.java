package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.entity.FeedingPlan;
import com.zoo.zims.mapper.FeedingPlanMapper;
import com.zoo.zims.service.FeedingPlanService;
import org.springframework.stereotype.Service;

@Service
public class FeedingPlanServiceImpl extends ServiceImpl<FeedingPlanMapper, FeedingPlan> implements FeedingPlanService {
}