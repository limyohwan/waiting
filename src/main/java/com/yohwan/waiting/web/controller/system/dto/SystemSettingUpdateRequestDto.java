package com.yohwan.waiting.web.controller.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SystemSettingUpdateRequestDto {
    private Long systemSettingId;
    @NotNull(message = "예상대기시간은 필수입니다.")
    @Min(value = 1, message = "예상대기시간은 최소 1분이상입니다.")
    private Integer expectedWaitTime;
}
