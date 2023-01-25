package com.wizlab.api.controller;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizlab.api.common.ApiResponse;
import com.wizlab.api.common.ErrorCode;
import com.wizlab.api.domain.LoginDto;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Manager-auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager-auth")
public class ManagerAuthController {
    
    /**
     * @url manager-auth/login
     * @처리형태 Post
     * @인터페이스ID MOLM_IF_001
     * @화면명 로그인
     * @인터페이스명 로그인
     */
    @Operation(summary="TPMS_IF_001", description = " - 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request){
        try {
            log.info("login");
            log.info("{}, {}", loginDto.getMgId(), loginDto.getMgPw());
            // ApiResponse result = managerAuthService.signin(loginDTO,request);
            ApiResponse result = new ApiResponse<>();
            return ResponseEntity.ok(result);
        }
        // } catch (CustomException ce) {
            // log.error("Custom Exception : {}", ce.getMessage());
            // return ResponseEntity.ok(new ApiResponse(ce.getCode()));
        // } 
        catch (Exception e) {
            log.error("Exception : {}", e);
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }

    }
}
