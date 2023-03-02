package com.yohwan.waiting.web.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("solapi.properties")
@RequiredArgsConstructor
public class SolapiProperties {
    private final String apiKey;
    private final String apiSecretKey;
    private final String domain;
    private final String pfId;
    private final String firstTemplateId;
    private final String secondTemplateId;
    private final String from;
}
