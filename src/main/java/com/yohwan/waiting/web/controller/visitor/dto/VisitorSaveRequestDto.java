package com.yohwan.waiting.web.controller.visitor.dto;

import com.yohwan.waiting.domain.visitor.VisitorType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class VisitorSaveRequestDto {
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "전화번호는 공백일 수 없습니다.")
    @Pattern(regexp = "[0-9]{10,11}", message = "번호는 10~11자리의 숫자만 입력가능합니다")
    private String phoneNumber;

    @NotNull(message = "인원은 공백일 수 없습니다.")
    private Integer peopleNumber;

    @NotNull(message = "방문타입은 공백일 수 없습니다.")
    private VisitorType visitorType;
    private Integer age;

    @Pattern(regexp = "M|F", message = "성별은 M 또는 F만 입력가능합니다.")
    private String gender;

    @NotNull(message = "개인정보 동의여부는 공백일 수 없습니다.")
    private Boolean isEnabledPersonalInfo;

    @NotNull(message = "마케팅 동의여부는 공백일 수 없습니다.")
    private Boolean isEnabledMarketingInfo;
}
