package com.new3seagull.SeagullsRoom.global.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private ExceptionCode exceptionCode;
    private String message;

    public CustomException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
        this.message = exceptionCode.getMessage();
    }

    public CustomException(ExceptionCode exceptionCode, String message) {
        this.exceptionCode = exceptionCode;
        this.message = message;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(message, exceptionCode.getHttpStatus());
    }

    public HttpStatus status(){
        return exceptionCode.getHttpStatus();
    }
}