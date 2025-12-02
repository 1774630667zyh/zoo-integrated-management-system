package com.zoo.zims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoo.zims.entity.MedicalRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {
}