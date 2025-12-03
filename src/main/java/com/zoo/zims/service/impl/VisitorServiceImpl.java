package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.entity.Visitor;
import com.zoo.zims.mapper.VisitorMapper;
import com.zoo.zims.service.VisitorService;
import com.zoo.zims.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        Visitor visitor = this.getOne(new LambdaQueryWrapper<Visitor>()
                .eq(Visitor::getUsername, loginDTO.getUsername()));

        if (visitor == null) {
            throw new RuntimeException("账号不存在");
        }

        String inputPwd = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!inputPwd.equals(visitor.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // Role设为 "visitor"
        String token = jwtUtils.generateToken(visitor.getId(), visitor.getUsername(), "visitor");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", visitor);
        return result;
    }

    @Override
    public void register(Visitor visitor) {
        long count = this.count(new LambdaQueryWrapper<Visitor>().eq(Visitor::getUsername, visitor.getUsername()));
        if (count > 0) {
            throw new RuntimeException("该账号已存在");
        }

        visitor.setPassword(DigestUtils.md5DigestAsHex(visitor.getPassword().getBytes(StandardCharsets.UTF_8)));
        if(visitor.getNickname() == null || visitor.getNickname().isEmpty()) {
            visitor.setNickname("游客" + System.currentTimeMillis() % 10000);
        }

        this.save(visitor);
    }
}