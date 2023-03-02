package com.yohwan.waiting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Primary
@Slf4j
@Service
public class MailSender implements MessageService{
    @Override
    public void sendMessage(String phoneNumber, int type, HashMap<String, String> variables) {
        log.info("mail sender send");
    }
}
