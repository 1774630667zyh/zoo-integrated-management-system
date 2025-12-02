package com.zoo.zims.config;

import com.zoo.zims.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求 (跨域预检)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 1. 获取Header中的Token
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去除 "Bearer " 前缀

            // 2. 解析Token
            Claims claims = jwtUtils.getClaimsByToken(token);
            if (claims != null) {
                // 验证通过，将用户信息存入Request域，方便后续Controller使用
                request.setAttribute("userId", claims.get("id"));
                request.setAttribute("role", claims.get("role"));
                return true;
            }
        }

        // 3. 验证失败
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"flag\":false,\"code\":20003,\"message\":\"未登录或Token过期\"}");
        return false;
    }
}