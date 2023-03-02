package com.yohwan.waiting.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;

public interface MessageService {
    void sendMessage(String phoneNumber, int type, HashMap<String, String> variables);
}
