package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 物种信息实体类
 * 对应数据库表: t_species
 */
@Data
@TableName("t_species")
public class Species implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学名 (如 Panthera tigris altaica)
     */
    private String scientificName;

    /**
     * 中文俗名 (如 东北虎)
     */
    private String commonName;

    /**
     * IUCN 保护等级 (LC, VU, EN, CR, EW, EX)
     */
    private String conservationStatus;

    /**
     * 食性 (肉食, 草食, 杂食)
     */
    private String dietType;
}