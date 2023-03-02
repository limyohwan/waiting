package com.yohwan.waiting.domain.visitor;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VisitorType {
    PREVIEW_HOPE("관람희망"),
    CONSULT_HOPE("계약희망");

    private final String title;

    VisitorType(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    @JsonCreator
    public static VisitorType from(String s) {
        return VisitorType.valueOf(s.toUpperCase());
    }
}
