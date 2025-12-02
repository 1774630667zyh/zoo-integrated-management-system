package com.zoo.zims.common;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 所有Controller的返回值均应为该类型
 * @param <T> 数据载体类型
 */
@Data
public class Result<T> {

    private boolean flag;    // 是否成功
    private Integer code;    // 返回码
    private String message;  // 返回信息
    private T data;          // 返回数据

    // 构造函数
    public Result(boolean flag, Integer code, String message, T data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    // 静态快捷方法 - 成功带数据
    public static <T> Result<T> success(T data) {
        return new Result<>(true, StatusCode.OK, "操作成功", data);
    }

    // 静态快捷方法 - 成功无数据
    public static Result<Void> success() {
        return new Result<>(true, StatusCode.OK, "操作成功");
    }

    // 静态快捷方法 - 失败
    // 修改点：添加 <T> 泛型声明，并返回 Result<T>，使其能适应所有 Controller 的返回类型
    public static <T> Result<T> error(String message) {
        return new Result<>(false, StatusCode.ERROR, message);
    }
}