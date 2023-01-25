package com.wizlab.api.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SuccessCode {
    
    CODE_SUCCESS(0, Boolean.TRUE,"success"),
    CODE_FALSE(1, Boolean.FALSE,"false");

    private Integer code;
    private Boolean trueAndFalse;
    private String msg;

    SuccessCode(Integer code, Boolean trueAndFalse,String msg){
        this.code = code;
        this.trueAndFalse = trueAndFalse;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }
    public Boolean getTrueAndFalse(){
        return trueAndFalse;
    }
    public String getMsg(){
        return msg;
    }

    public static SuccessCode getSuccessCodeByCode(Integer code) {
        return Arrays.stream(SuccessCode.values()).filter(x -> x.getCode().equals(code)).findFirst().get();
    }
}
