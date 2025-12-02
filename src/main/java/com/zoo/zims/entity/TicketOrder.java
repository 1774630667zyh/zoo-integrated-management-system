package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 票务订单实体类
 * 对应数据库表: t_ticket_order
 */
@Data
@TableName(value = "t_ticket_order", autoResultMap = true) // autoResultMap 必须开启以支持 TypeHandler
public class TicketOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号 (主键, 非自增, 需自行生成)
     */
    @TableId(type = IdType.INPUT)
    private String orderNo;

    /**
     * 用户ID (可选)
     */
    private Long userId;

    /**
     * 联系手机
     */
    private String mobile;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付状态
     * 0:未支付, 1:已支付, 2:已退款
     */
    private Integer payStatus;

    /**
     * 预约入园日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitDate;

    /**
     * 票据详情快照 (JSON格式)
     * 例如: [{"type":"成人票","price":100,"count":2}]
     * 需要在 MyBatis Plus 配置中配合 JacksonTypeHandler 使用
     */
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private Object ticketJson;

    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}