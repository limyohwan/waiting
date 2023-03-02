package com.yohwan.waiting.service;

import com.yohwan.waiting.web.properties.SolapiProperties;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class MessageSender implements MessageService{
//    private final DefaultMessageService messageService;
    private final SolapiProperties solapiProperties;
    public MessageSender(SolapiProperties solapiProperties){
//        this.messageService = NurigoApp.INSTANCE.initialize(solapiProperties.getApiKey(), solapiProperties.getApiSecretKey(), solapiProperties.getDomain());
        this.solapiProperties = solapiProperties;
    }

    @Override
    public void sendMessage(String phoneNumber, int type, HashMap<String, String> variables) {
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(solapiProperties.getPfId());
        kakaoOption.setTemplateId(type == 1 ? solapiProperties.getFirstTemplateId() : solapiProperties.getSecondTemplateId());
        kakaoOption.setVariables(variables);

        Message message = new Message();
        message.setFrom(solapiProperties.getFrom());
        message.setTo(phoneNumber);
        message.setKakaoOptions(kakaoOption);

//        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
//        log.info("singleMessageSentResponse : {}", response);
    }
}
