package com.zoo.zims.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和解析Token
 */
@Component
public class JwtUtils {

    // 秘钥 (实际开发中应存储在配置文件中)
    private static final String SECRET_KEY = "zoo_secret_key_123456";

    // 过期时间: 24小时
    private static final long EXPIRATION_TIME = 86400000L;

    /**
     * 生成Token
     * @param userId 用户ID
     * @param username 用户名
     * @param role 角色
     * @return Token字符串
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析Token获取Claims
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null; // 解析失败或过期
        }
    }
}