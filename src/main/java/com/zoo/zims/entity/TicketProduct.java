package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 票种信息实体
 */
@Data
@TableName("t_ticket_product")
public class TicketProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private BigDecimal price;

    private String description;

    // 1:上架, 0:下架
    private Integer status;
}