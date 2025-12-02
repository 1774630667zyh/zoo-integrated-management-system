package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_maintenance")
public class Maintenance implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer facilityId;

    private String description;

    private Long reporterId;

    // 0:待处理, 1:维修中, 2:已完成
    private Integer status;

    private String result;

    private LocalDateTime createTime;

    private LocalDateTime resolveTime;
}