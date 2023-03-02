package com.yohwan.waiting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MessageSenderTest {
    @Autowired
    MessageSender messageSender;

    @Test
    public void 카카오톡알람테스트(){
        Long currentWaitCount = 100L;
        //카카오 알람 로직
        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{visitor}", "임요환");
        variables.put("#{currentUser}", String.valueOf(currentWaitCount));
//        messageSender.sendMessage("01012345678", 1, variables);
    }

    @Test
    public void 문자테스트(){
//        messageSender.sendSms("01012345678");
    }
}