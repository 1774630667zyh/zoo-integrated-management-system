package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_visitor")
public class Visitor implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @JsonIgnore // JSON序列化时忽略密码
    private String password;

    private String nickname;

    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}