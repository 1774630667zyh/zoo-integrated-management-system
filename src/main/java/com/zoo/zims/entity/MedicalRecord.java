package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医疗记录实体类
 * 对应数据库表: t_medical_record
 */
@Data
@TableName("t_medical_record")
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联动物ID
     */
    private Long animalId;

    /**
     * 主治兽医ID
     */
    private Long vetId;

    /**
     * 就诊时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime visitDate;

    /**
     * 症状描述
     */
    private String symptoms;

    /**
     * 诊断结果
     */
    private String diagnosis;

    /**
     * 治疗方案/用药
     */
    private String treatment;

    /**
     * 建议复诊日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextVisit;
}