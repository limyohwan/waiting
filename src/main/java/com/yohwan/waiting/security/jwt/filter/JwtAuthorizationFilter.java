package com.yohwan.waiting.security.jwt.filter;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.repository.member.MemberRepository;
import com.yohwan.waiting.security.auth.AuthMember;
import com.yohwan.waiting.security.auth.PrincipalDetails;
import com.yohwan.waiting.security.jwt.JwtProvider;
import com.yohwan.waiting.web.exception.custom.AuthException;
import com.yohwan.waiting.web.exception.custom.MemberException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String jwt= getJwtByHeader(request);

        try{
            validateJwt(jwt);
            Map<String, Claim> claims = jwtProvider.getClaims(jwt);
            String username = claims.get("username").asString();
//          Long id = claims.get("id").asLong();
//          String roleType = claims.get("roleType").asString();
            setSecurityContext(username);
        }catch (TokenExpiredException e){
            request.setAttribute("exception", "tokenExpired");
        }catch (Exception e){
            request.setAttribute("exception", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private void validateJwt(String jwt) {
        if(StringUtils.hasText(jwt)){
            jwtProvider.validateJwt(jwt);
        }else{
            throw new AuthException("토큰형식이 옳바르지않습니다.");
        }
    }

    private void setSecurityContext(String username) {
        if(username != null){
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new MemberException("user가 등록되어 있지 않습니다. username : " + username));

            AuthMember authMember = new AuthMember(member);
            PrincipalDetails principalDetails = new PrincipalDetails(authMember);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String getJwtByHeader(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
            jwt = jwt.replace("Bearer ", "");
            return jwt;
        }
        return null;
    }
}
