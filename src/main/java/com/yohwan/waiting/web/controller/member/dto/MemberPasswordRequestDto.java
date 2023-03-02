package com.yohwan.waiting.web.controller.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class MemberPasswordRequestDto {
    @NotBlank(message = "변경할 비밀번호는 필수입니다.")
    private String password;
}