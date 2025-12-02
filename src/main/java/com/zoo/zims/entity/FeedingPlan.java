package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 喂养计划实体
 */
@Data
@TableName("t_feeding_plan")
public class FeedingPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long animalId;

    private Integer foodId;

    private BigDecimal amount;

    private LocalTime feedingTime; // 每日投喂时间

    private Long feederId; // 负责人

    private LocalDateTime createTime;
}