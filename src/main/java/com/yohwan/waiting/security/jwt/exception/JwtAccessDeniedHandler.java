package com.yohwan.waiting.security.jwt.exception;

import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ObjectMapper om = new ObjectMapper();
        ApiResponseDto apiResponse = new ApiResponseDto(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
        response.getWriter().println(om.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}
