package com.yohwan.waiting.service;

import com.yohwan.waiting.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 10;
    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter connect(Long userId, String lastEventId, boolean tokenChecker){
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> sseEmitterRepository.deleteByEmitterId(emitterId));
        emitter.onTimeout(() -> {
            emitter.complete();
            sseEmitterRepository.deleteByEmitterId(emitterId);
        });
        if(tokenChecker){
            sendToClient(emitter, emitterId, "sse connect success. [userId : " + userId + "]");
        }else{
            sendToClient(emitter, emitterId, "tokenExpired");
        }

        return emitter;
    }

    public void sendAllConnectedClient(Object data){
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitters();
        emitters.forEach((id, emitter) -> {
            log.info("{} = {} ",id,emitter);
            sendToClient(emitter, id, data);
        });
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("result")
                    .data(data));
        } catch (IOException e) {
            log.info(e.getMessage());
            emitter.complete();
            sseEmitterRepository.deleteByEmitterId(emitterId);
        }
    }
}
