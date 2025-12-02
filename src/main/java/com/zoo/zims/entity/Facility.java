package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_facility")
public class Facility implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private String location;

    // 1:开放, 0:关闭, 2:维护中
    private Integer status;

    private Integer capacity;

    private LocalDateTime createTime;
}