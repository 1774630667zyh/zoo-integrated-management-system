package com.zoo.zims.dto;

import lombok.Data;

/**
 * 登录请求参数封装
 */
@Data
public class LoginDTO {
    private String username;
    private String password;
}