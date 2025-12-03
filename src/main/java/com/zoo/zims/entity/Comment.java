package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long visitorId;

    private String nickname; // 冗余字段

    private String type; // 'animal' 或 'facility'

    private Long targetId; // 关联的动物或场馆ID

    private String content;

    private Integer rating;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}