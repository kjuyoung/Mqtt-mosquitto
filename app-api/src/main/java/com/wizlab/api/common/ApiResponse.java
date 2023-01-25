package com.wizlab.api.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse<T> implements Serializable {
    
    private Boolean success;
    private String message;
    private Integer code;
    private T data;

    public ApiResponse(Boolean success){
        this.success = success;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ApiResponse(Boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(Boolean success, String message, Integer code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ApiResponse(Boolean success, Integer code) {
        this.success = success;
        this.code = code;
    }

    public ApiResponse(Boolean success, String message, Integer code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public ApiResponse(ErrorCode errorCode){
        this.success = false;
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.data = null;
    }

    public ApiResponse(ErrorCode errorCode,T data){
        this.success = false;
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.data = data;
    }

    public ApiResponse(SuccessCode successCode,T data){
        this.message = successCode.getMsg();
        this.success = successCode.getTrueAndFalse();
        this.code = successCode.getCode();
        this.data = data;
    }
}
