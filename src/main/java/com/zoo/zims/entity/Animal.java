package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 动物基础信息实体类
 * 对应数据库表: t_animal
 */
@Data
@TableName("t_animal")
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * 使用数据库自增策略
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 动物昵称
     */
    private String name;

    /**
     * 全球唯一标识符 (Global ID)
     */
    private String globalId;

    /**
     * 芯片代码 (RFID)
     */
    private String chipCode;

    /**
     * 所属物种ID
     */
    private Integer speciesId;

    /**
     * 性别
     * 1:雄性, 0:雌性, 2:未知
     */
    private Integer gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * 父亲ID (Sire)
     */
    private Long sireId;

    /**
     * 母亲ID (Dam)
     */
    private Long damId;

    /**
     * 所在场馆ID
     */
    private Integer enclosureId;

    /**
     * 状态
     * 1:在园, 2:借出, 3:死亡, 4:已出售
     */
    private Integer status;

    /**
     * 创建时间
     * 自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}