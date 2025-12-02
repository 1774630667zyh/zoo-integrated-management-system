package com.zoo.zims.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 (适配 jjwt 0.11.x 版本)
 * 修复了 WeakKeyException 和 DecodingException 问题
 */
@Component
public class JwtUtils {

    // 1. 秘钥必须足够长（至少256位/32字节），否则会报错 WeakKeyException
    // 2. 使用普通字符串而非Base64编码字符串，避免解码错误
    private static final String SECRET_STRING = "zoo_management_system_secret_key_must_be_long_enough_for_security_123456";

    // 过期时间: 24小时
    private static final long EXPIRATION_TIME = 86400000L;

    /**
     * 获取签名Key对象
     * 使用 Keys.hmacShaKeyFor 将普通字符串直接转为 Key，
     * 避免了 jjwt 默认尝试 Base64 解码导致的 "Illegal base64 character" 错误
     */
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_STRING.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成Token
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
                // 关键修改：传入 Key 对象，而不是字符串
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析Token获取Claims
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parserBuilder() // 0.11.x 推荐使用 parserBuilder
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 解析失败（过期、篡改、格式错误等）
            return null;
        }
    }
}