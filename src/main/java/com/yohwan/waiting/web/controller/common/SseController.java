package com.yohwan.waiting.web.controller.common;

import com.yohwan.waiting.repository.SseEmitterRepository;
import com.yohwan.waiting.security.jwt.JwtProvider;
import com.yohwan.waiting.service.SseService;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;
    private final SseEmitterRepository sseEmitterRepository;
    private final JwtProvider jwtProvider;

    @GetMapping(value = "/api/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestParam String token, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        boolean tokenChecker = validateJwt(token);
        Map<String, Claim> decodeJwt = jwtProvider.decodeJwt(token.replace("Bearer ", ""));
        SseEmitter sseEmitter = sseService.connect(decodeJwt.get("id").asLong(), lastEventId, tokenChecker);
        return sseEmitter;
    }

    private boolean validateJwt(String token) {
        if(!StringUtils.hasText(token)){
            log.info("토큰값이 없습니다.");
            return false;
        }
        boolean checker = true;
        try{
            jwtProvider.validateJwt(token.replace("Bearer ", ""));
        }catch(Exception e){
            checker = false;
            log.info("토큰값이 옳바르지 않습니다. token = {}", token);
        }
        return checker;
    }
}
