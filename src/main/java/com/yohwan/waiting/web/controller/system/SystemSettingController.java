package com.yohwan.waiting.web.controller.system;

import com.yohwan.waiting.service.SystemSettingService;
import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.yohwan.waiting.web.controller.system.dto.SystemSettingResponseDto;
import com.yohwan.waiting.web.controller.system.dto.SystemSettingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SystemSettingController {
    private final SystemSettingService systemSettingService;

    @GetMapping("/api/system-setting")
    public ApiResponseDto<SystemSettingResponseDto> findSystemSetting(){
        return new ApiResponseDto(HttpStatus.OK, systemSettingService.findSystemSetting());
    }

    @PostMapping("/api/system-setting")
    public ApiResponseDto<Long> createOrChangeExpectedWaitTime(@RequestBody @Valid SystemSettingUpdateRequestDto systemSettingUpdateRequestDto){
        return new ApiResponseDto(HttpStatus.OK, systemSettingService.createOrChangeExpectedWaitTime(systemSettingUpdateRequestDto));
    }
}
