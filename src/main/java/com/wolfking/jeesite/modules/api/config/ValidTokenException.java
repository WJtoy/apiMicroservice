package com.wolfking.jeesite.modules.api.config;

/**
 * 非法验证码异常
 */
public class ValidTokenException extends RuntimeException {

    public ValidTokenException() {
    }

    public ValidTokenException(String message) {
        super(message);
    }

    public ValidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
