package com.zoo.zims.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类
 * 对应数据库表: t_staff
 */
@Data
@TableName("t_staff")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名 (工号)
     */
    private String username;

    /**
     * 密码 (加密存储)
     * JSON序列化时忽略该字段，防止密码泄露
     */
    @JsonIgnore
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 角色 (admin, vet, feeder, ticket_seller)
     */
    private String role;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态 (1:启用, 0:禁用)
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}