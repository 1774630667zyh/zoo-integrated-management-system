package com.zoo.zims.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知异常
     * 返回类型修改为 Result<?> 以兼容 Result<Void>
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        logger.error("系统异常:", e);
        // Result.error() 通常返回 Result<Void>
        return Result.error("系统繁忙，请稍后再试: " + e.getMessage());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        logger.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }
}