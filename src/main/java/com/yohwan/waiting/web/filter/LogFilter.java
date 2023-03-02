package com.yohwan.waiting.web.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Component
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String logId = UUID.randomUUID().toString();
        MDC.put("LOG_ID", logId);
        chain.doFilter(request, response);
    }
}
