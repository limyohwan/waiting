package com.yohwan.waiting.web.controller.system.dto;

import com.yohwan.waiting.domain.system.SystemSetting;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SystemSettingResponseDto {
    private Long id;
    private Integer expectedWaitTime;

    public SystemSettingResponseDto(SystemSetting systemSetting){
        this.id = systemSetting.getId();
        this.expectedWaitTime = systemSetting.getExpectedWaitTime();
    }
}
