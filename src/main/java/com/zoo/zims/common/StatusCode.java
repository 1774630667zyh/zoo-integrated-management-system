package com.zoo.zims.common;

/**
 * 状态码枚举类
 * 用于统一定义API返回的状态码
 */
public class StatusCode {

    public static final int OK = 20000;          // 成功
    public static final int ERROR = 20001;       // 失败
    public static final int LOGIN_ERROR = 20002; // 用户名或密码错误
    public static final int ACCESS_ERROR = 20003;// 权限不足
    public static final int REMOTE_ERROR = 20004;// 远程调用失败
    public static final int REP_ERROR = 20005;   // 重复操作

    // 业务相关错误码
    public static final int ANIMAL_NOT_FOUND = 30001; // 未找到动物信息
    public static final int STOCK_NOT_ENOUGH = 40001; // 库存不足 (饲料/门票)
}