package com.yohwan.waiting.service;


import com.yohwan.waiting.domain.auth.RefreshToken;
import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.repository.RefreshTokenRepository;
import com.yohwan.waiting.web.controller.auth.dto.AuthLoginDto;
import com.yohwan.waiting.web.controller.auth.dto.JwtDto;
import com.yohwan.waiting.web.exception.custom.AuthException;
import com.yohwan.waiting.web.controller.member.dto.MemberSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    MemberService memberService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test(expected = AuthException.class)
    public void 토큰발급및재발급(){
        Long memberId = saveMember("yhlim", "1234", "임요환");
        AuthLoginDto loginDto = new AuthLoginDto();
        loginDto.setUsername("yhlim");
        loginDto.setPassword("1234");
        JwtDto jwtDto = authService.login(loginDto);

        Assertions.assertThat(jwtDto.getAccessToken()).isNotEmpty();
        Assertions.assertThat(jwtDto.getRefreshToken()).isNotEmpty();

        JwtDto newJwtDto = authService.reissue(jwtDto);
        Assertions.assertThat(newJwtDto.getAccessToken()).isNotEmpty();
        Assertions.assertThat(newJwtDto.getRefreshToken()).isNotEmpty();

        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndMemberId(jwtDto.getRefreshToken(), memberId).get();
        refreshTokenRepository.delete(refreshToken);

        JwtDto newJwtDto2 = authService.reissue(jwtDto);
    }

    private Long saveMember(String username, String password, String name){
        MemberSaveRequestDto requestDto = new MemberSaveRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);
        requestDto.setName(name);
        requestDto.setRoleType(RoleType.ROLE_MANAGER);
        requestDto.setMemberStatus(MemberStatus.VALID);
        return memberService.save(requestDto);
    }

}