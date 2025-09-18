package com.xiaoshao.exception;
//数据库错误
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message);
    }
}
