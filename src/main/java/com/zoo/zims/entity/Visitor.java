package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * [修复]
     * 将 @JsonIgnore 改为 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
     * 作用：允许前端传入密码进行注册/登录，但在返回用户信息时隐藏密码。
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}