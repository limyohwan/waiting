package com.yohwan.waiting.web.controller.common;

import com.yohwan.waiting.service.CodeService;
import com.yohwan.waiting.web.controller.common.dto.CodeDto;
import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CodeController {
    private final CodeService codeService;

    @GetMapping("/api/codes")
    public ApiResponseDto<Map<String, List<CodeDto>>> getCodes(){
        Map<String, List<CodeDto>> result = new HashMap<>();
        result.put("roleType" , codeService.getRoleTypes());
        result.put("visitorStatus", codeService.getVisitorStatus());
        result.put("visitorType", codeService.getVisitorType());
        result.put("memberStatus", codeService.getMemberStatus());
        return new ApiResponseDto(HttpStatus.OK,result);
    }

    @GetMapping("/api/codes/role-type")
    public ApiResponseDto<List<CodeDto>> getRoleType(){
        return new ApiResponseDto(HttpStatus.OK,codeService.getRoleTypes());
    }

    @GetMapping("/api/codes/visitor-status")
    public ApiResponseDto<List<CodeDto>> getVisitorStatus(){
        return new ApiResponseDto(HttpStatus.OK, codeService.getVisitorStatus());
    }

    @GetMapping("/api/codes/visitor-type")
    public ApiResponseDto<List<CodeDto>> getVisitorType(){
        return new ApiResponseDto(HttpStatus.OK, codeService.getVisitorType());
    }

    @GetMapping("/api/codes/member-status")
    public ApiResponseDto<List<CodeDto>> getMemberStatus(){
        return new ApiResponseDto(HttpStatus.OK, codeService.getMemberStatus());
    }
}
