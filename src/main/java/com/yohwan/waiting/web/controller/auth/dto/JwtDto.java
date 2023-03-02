package com.yohwan.waiting.web.controller.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class JwtDto {
    @NotNull(message = "토큰값은 필수입니다.")
    private String accessToken;
    @NotNull(message = "리프레쉬 토큰값은 필수입니다.")
    private String refreshToken;

    @Builder
    public JwtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}