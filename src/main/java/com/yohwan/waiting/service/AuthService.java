package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.auth.RefreshToken;
import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.repository.RefreshTokenRepository;
import com.yohwan.waiting.repository.member.MemberRepository;
import com.yohwan.waiting.security.auth.PrincipalDetails;
import com.yohwan.waiting.security.jwt.JwtProvider;
import com.yohwan.waiting.web.controller.auth.dto.AuthLoginDto;
import com.yohwan.waiting.web.controller.auth.dto.JwtDto;
import com.yohwan.waiting.web.exception.custom.AuthException;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public JwtDto login(AuthLoginDto authLoginDto){
        Authentication authentication = getAuthentication(authLoginDto);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        JwtDto jwtDto = jwtProvider.createTokenDto(principalDetails);
        Member member = memberRepository.findById(principalDetails.getMember().getId()).get();
        refreshTokenRepository.save(RefreshToken.builder()
                .token(jwtDto.getRefreshToken())
                .member(member)
                .build());

        return jwtDto;
    }

    private Authentication getAuthentication(AuthLoginDto authLoginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authLoginDto.getUsername(), authLoginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        return authentication;
    }

    public JwtDto reissue(JwtDto jwtDto){
        jwtProvider.validateJwt(jwtDto.getRefreshToken());

        Map<String, Claim> claims = getJwtAttribute(jwtDto.getAccessToken());
        Long id = claims.get("id").asLong();
        String username = claims.get("username").asString();
        String roleType = claims.get("roleType").asString();
        refreshTokenRepository.findByTokenAndMemberId(jwtDto.getRefreshToken(), id)
                .orElseThrow(()-> new AuthException("리프레쉬토큰이 옳바르지 않습니다."));

        return JwtDto.builder()
                .accessToken(jwtProvider.createAccessToken(username, id, roleType))
                .refreshToken(jwtDto.getRefreshToken())
                .build();
    }

    private Map<String, Claim> getJwtAttribute(String accessToken) {
        if(StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")){
            Map<String, Claim> claims = jwtProvider.decodeJwt(accessToken.replace("Bearer ", ""));
            return claims;
        }else{
            return null;
        }
    }
}
