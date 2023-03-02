package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.system.SystemSetting;
import com.yohwan.waiting.repository.SystemSettingRepository;
import com.yohwan.waiting.web.exception.custom.SystemSettingException;
import com.yohwan.waiting.web.controller.system.dto.SystemSettingResponseDto;
import com.yohwan.waiting.web.controller.system.dto.SystemSettingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SystemSettingService {
    private final SystemSettingRepository systemSettingRepository;
    @Transactional(readOnly = true)
    public SystemSettingResponseDto findSystemSetting(){
        List<SystemSetting> result = systemSettingRepository.findAll();
        return result.size() != 0 ? new SystemSettingResponseDto(result.get(0)) : null;
    }

    public Long createOrChangeExpectedWaitTime(SystemSettingUpdateRequestDto systemSettingUpdateRequestDto){
        return systemSettingUpdateRequestDto.getSystemSettingId() == null ? createSystemSetting(systemSettingUpdateRequestDto) : changeSystemSetting(systemSettingUpdateRequestDto);
    }

    private Long changeSystemSetting(SystemSettingUpdateRequestDto systemSettingUpdateRequestDto) {
        SystemSetting systemSetting = systemSettingRepository.findById(systemSettingUpdateRequestDto.getSystemSettingId())
                .orElseThrow(() -> new SystemSettingException("시스템설정이 등록되어 있지않습니다"));
        systemSetting.changeExpectedWaitTime(systemSettingUpdateRequestDto.getExpectedWaitTime());
        return systemSetting.getId();
    }

    private Long createSystemSetting(SystemSettingUpdateRequestDto systemSettingUpdateRequestDto) {
        return systemSettingRepository.save(SystemSetting.builder()
                .expectedWaitTime(systemSettingUpdateRequestDto.getExpectedWaitTime())
                .build()).getId();
    }
}
