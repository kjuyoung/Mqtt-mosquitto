package com.wizlab.api.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    
    CODE_0000(0000, "success")

    ,CODE_500(500, "Internal error")

    /* token, login, signup */
    ,CODE_9000(9000, "토큰이 만료 되었습니다.")
    ,CODE_9001(9001, "토큰 변환 오류")
    ,CODE_9002(9002, "jwt 토큰이 없습니다.")
    ,CODE_9003(9003, "리프레시 토큰이 아닙니다.")
    ,CODE_9004(9004, "액세스 토큰이 아닙니다.")
    ,CODE_9005(9005, "회원정보를 찾을 수 없습니다.")
    ,CODE_9006(9006, "로그인 토큰이 아닙니다.")
    ,CODE_9007(9007, "이메일 인증이 필요합니다.")
    ,CODE_9008(9008, "다시 로그인을 해주세요.")
    ;
    
    private Integer code;
    private String msg;

    ErrorCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

    public static ErrorCode getErrorCodeByCode(Integer code) {
        return Arrays.stream(ErrorCode.values()).filter(x -> x.getCode().equals(code)).findFirst().get();
    }
}
