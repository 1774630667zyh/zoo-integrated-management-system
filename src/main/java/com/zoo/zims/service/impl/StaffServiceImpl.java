package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.entity.Staff;
import com.zoo.zims.mapper.StaffMapper;
import com.zoo.zims.service.StaffService;
import com.zoo.zims.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        // 1. 根据用户名查询
        Staff staff = this.getOne(new LambdaQueryWrapper<Staff>()
                .eq(Staff::getUsername, loginDTO.getUsername()));

        if (staff == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 校验密码 (假设使用MD5加密，实际建议使用BCrypt)
        String inputPwdMd5 = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!inputPwdMd5.equals(staff.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (staff.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        // 3. 生成Token
        String token = jwtUtils.generateToken(staff.getId(), staff.getUsername(), staff.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", staff); // Staff实体中的password加了@JsonIgnore，不会返回
        return result;
    }

    @Override
    public boolean createStaff(Staff staff) {
        // 校验用户名重复
        long count = this.count(new LambdaQueryWrapper<Staff>().eq(Staff::getUsername, staff.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 密码加密存储
        String rawPwd = staff.getPassword();
        if (rawPwd == null) {
            rawPwd = "123456"; // 默认密码
        }
        staff.setPassword(DigestUtils.md5DigestAsHex(rawPwd.getBytes(StandardCharsets.UTF_8)));

        if (staff.getStatus() == null) {
            staff.setStatus(1);
        }

        return this.save(staff);
    }
}