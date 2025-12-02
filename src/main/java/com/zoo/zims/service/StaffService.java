package com.zoo.zims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.entity.Staff;

import java.util.Map;

public interface StaffService extends IService<Staff> {

    /**
     * 员工登录
     * @param loginDTO 登录参数
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> login(LoginDTO loginDTO);

    /**
     * 创建新员工
     */
    boolean createStaff(Staff staff);
}