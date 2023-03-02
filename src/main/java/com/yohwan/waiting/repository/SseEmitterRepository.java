package com.yohwan.waiting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter sseEmitter){
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    public Map<String, SseEmitter> findAllEmitters(){
        return emitters;
    }

    public void deleteByEmitterId(String emitterId){
        emitters.remove(emitterId);
    }

}
