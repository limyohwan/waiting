package com.yohwan.waiting.web.controller.auth;

import com.yohwan.waiting.security.jwt.properties.JwtProperties;
import com.yohwan.waiting.service.AuthService;
import com.yohwan.waiting.service.MemberService;
import com.yohwan.waiting.web.controller.auth.dto.AuthLoginDto;
import com.yohwan.waiting.web.controller.auth.dto.JwtDto;
import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.yohwan.waiting.web.controller.member.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ApiResponseDto<JwtDto> login(@RequestBody @Valid AuthLoginDto authLoginDto, HttpServletResponse response){
        JwtDto jwtDto = authService.login(authLoginDto);
//        setCookie(response, jwtDto);
        return new ApiResponseDto(HttpStatus.OK, jwtDto);
    }

    @PostMapping("/api/auth/reissue")
    public ApiResponseDto<JwtDto> reissue(@RequestBody @Valid JwtDto jwtDto){
        return new ApiResponseDto(HttpStatus.OK,authService.reissue(jwtDto));
    }

    @PostMapping("/api/auth/signup")
    public ApiResponseDto<Long> signup(@RequestBody @Valid MemberSaveRequestDto memberSaveRequestDto){
        return new ApiResponseDto(HttpStatus.OK,memberService.save(memberSaveRequestDto));
    }

    private void setCookie(HttpServletResponse response, JwtDto jwtDto) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtDto.getRefreshToken())
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
