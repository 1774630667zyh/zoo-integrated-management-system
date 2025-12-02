package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 饲料库存实体
 */
@Data
@TableName("t_food")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type; // 肉类, 蔬果, 干草...

    private BigDecimal stock;

    private String unit; // kg, 个...

    private BigDecimal minThreshold; // 预警阈值

    private LocalDateTime updateTime;
}