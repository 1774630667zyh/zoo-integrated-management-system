package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.entity.Staff;
import com.zoo.zims.mapper.StaffMapper;
import com.zoo.zims.service.StaffService;
import com.zoo.zims.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工业务逻辑实现类
 * 负责处理登录认证、员工增删改查等逻辑
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    // 日志对象，用于在控制台打印调试信息
    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        logger.info("=== 开始登录流程: 用户名 [{}] ===", loginDTO.getUsername());

        // 1. 根据用户名查询数据库
        Staff staff = this.getOne(new LambdaQueryWrapper<Staff>()
                .eq(Staff::getUsername, loginDTO.getUsername()));

        if (staff == null) {
            logger.warn("登录失败: 用户不存在");
            throw new RuntimeException("用户不存在");
        }

        // 2. 校验密码 (MD5加密比对)
        String inputPwdMd5 = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        logger.info("输入密码MD5: {}", inputPwdMd5);
        logger.info("数据库密码: {}", staff.getPassword());

        if (!inputPwdMd5.equals(staff.getPassword())) {
            logger.warn("登录失败: 密码错误");
            throw new RuntimeException("密码错误");
        }

        // 3. 检查账号状态
        if (staff.getStatus() == 0) {
            logger.warn("登录失败: 账号被禁用");
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 生成Token并返回
        try {
            String token = jwtUtils.generateToken(staff.getId(), staff.getUsername(), staff.getRole());
            logger.info("登录成功: Token已生成");

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userInfo", staff);
            return result;
        } catch (Exception e) {
            logger.error("Token生成失败", e);
            throw new RuntimeException("系统错误: Token生成失败");
        }
    }

    @Override
    public boolean createStaff(Staff staff) {
        // 校验用户名是否重复
        long count = this.count(new LambdaQueryWrapper<Staff>().eq(Staff::getUsername, staff.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 密码加密存储 (默认密码处理)
        String rawPwd = staff.getPassword();
        if (rawPwd == null) {
            rawPwd = "123456"; // 默认密码
        }
        staff.setPassword(DigestUtils.md5DigestAsHex(rawPwd.getBytes(StandardCharsets.UTF_8)));

        if (staff.getStatus() == null) {
            staff.setStatus(1); // 默认启用
        }

        return this.save(staff);
    }
}