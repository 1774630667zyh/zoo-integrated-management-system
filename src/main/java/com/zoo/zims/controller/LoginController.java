package com.zoo.zims.controller;

import com.zoo.zims.common.Result;
import com.zoo.zims.common.StatusCode;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        try {
            Map<String, Object> data = staffService.login(loginDTO);
            return Result.success(data);
        } catch (Exception e) {
            return new Result<>(false, StatusCode.LOGIN_ERROR, e.getMessage());
        }
    }

    // 登出通常由前端清除Token即可，后端可选实现黑名单机制
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}