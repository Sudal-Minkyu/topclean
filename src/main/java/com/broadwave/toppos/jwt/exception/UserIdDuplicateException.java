package com.broadwave.toppos.jwt.exception;

import lombok.Getter;

@Getter
public class UserIdDuplicateException extends RuntimeException{

    private ErrorCode errorCode;

    public UserIdDuplicateException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}