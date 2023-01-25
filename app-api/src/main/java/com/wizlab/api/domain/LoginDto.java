package com.wizlab.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@ApiModel(value = "로그인")
public class LoginDto {
    
    @ApiModelProperty(value = "아이디", required = true)
    // @Schema(description = "로그인 ID")
    private String mgId;

    @ApiModelProperty(value = "패스워드", required = true)
    // @Schema(description = "로그인 password")
    private String mgPw;
}
