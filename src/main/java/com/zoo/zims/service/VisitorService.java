package com.zoo.zims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoo.zims.dto.LoginDTO;
import com.zoo.zims.entity.Visitor;
import java.util.Map;

public interface VisitorService extends IService<Visitor> {
    Map<String, Object> login(LoginDTO loginDTO);
    void register(Visitor visitor);
}