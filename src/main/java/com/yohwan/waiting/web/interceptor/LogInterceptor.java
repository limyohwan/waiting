package com.yohwan.waiting.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String authorization = request.getHeader("Authorization");

        log.info("[requestURI={} {}][queryString={}][authorization={}][controller={}]", requestMethod, requestURI, queryString, authorization, handler);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        log.info("[requestURI={} {}][responseStatus={}]", requestMethod, requestURI, response.getStatus());
    }
}
