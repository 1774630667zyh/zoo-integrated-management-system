package com.zoo.zims.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**") // 拦截所有API
                .excludePathPatterns(
                        "/api/login",           // 管理员登录

                        // --- 游客端公开接口 ---
                        "/api/visitor/login",           // 游客登录
                        "/api/visitor/register",        // 游客注册
                        "/api/visitor/products",        // 浏览票种
                        "/api/visitor/animals",         // 浏览动物
                        "/api/visitor/facilities",      // 浏览场馆
                        "/api/visitor/orders",          // 简单查单
                        "/api/visitor/comments/list/**" // 查看评论
                );
        // 注意：/api/visitor/comment (POST) 发表评论接口没有排除，因此会被拦截校验Token
    }

    // 全局跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}