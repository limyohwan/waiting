package com.yohwan.waiting.security.jwt.exception;

import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ObjectMapper om = new ObjectMapper();
        checkTokenExpired(request, response, authException, om);
    }

    private void checkTokenExpired(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException, ObjectMapper om) throws IOException {
        if("tokenExpired".equals(request.getAttribute("exception"))){
            ApiResponseDto apiResponse = new ApiResponseDto(999, (String) request.getAttribute("exception"));
            response.getWriter().println(om.writeValueAsString(apiResponse));
            response.getWriter().flush();
        }else{
            ApiResponseDto apiResponse = new ApiResponseDto(HttpStatus.UNAUTHORIZED, authException.getMessage());
            response.getWriter().println(om.writeValueAsString(apiResponse));
            response.getWriter().flush();
        }
    }
}
