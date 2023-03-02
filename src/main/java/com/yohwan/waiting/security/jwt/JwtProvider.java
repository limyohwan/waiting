package com.yohwan.waiting.security.jwt;

import com.yohwan.waiting.security.auth.PrincipalDetails;
import com.yohwan.waiting.security.jwt.properties.JwtProperties;
import com.yohwan.waiting.web.controller.auth.dto.JwtDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final JwtProperties jwtProperties;

    public JwtDto createTokenDto(PrincipalDetails principalDetails){
        String roleType = principalDetails.getMember().getRoleType() != null ? principalDetails.getMember().getRoleType().name() : null;
        String accessToken = createAccessToken(
                principalDetails.getMember().getUsername(),
                principalDetails.getMember().getId(),
                roleType
        );

        String refreshToken = createRefreshToken();

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createRefreshToken() {
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiredAt()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
        return refreshToken;
    }

    public String createAccessToken(String username, Long id, String roleType){
        return "Bearer " + JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpiredAt()))
                .withClaim("id", id)
                .withClaim("username", username)
                .withClaim("roleType", roleType)
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
    }

    public Map<String, Claim> decodeJwt(String jwt){
        return JWT.decode(jwt).getClaims();
    }

    public void validateJwt(String jwt){
        JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build()
                .verify(jwt);
    }

    public Map<String, Claim> getClaims(String jwt){
        Map<String, Claim> claims = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build()
                .verify(jwt)
                .getClaims();
        return claims;
    }
}
