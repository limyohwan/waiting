package com.yohwan.waiting.domain.visitor;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VisitorStatus {
    WAIT("대기중"),
    PREVIEW("관람중"),
    CONSULT("상담중"),
    END("종료"),
    DENY("방문포기");

    private final String title;
    VisitorStatus(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    @JsonCreator
    public static VisitorStatus from(String s) {
        return VisitorStatus.valueOf(s.toUpperCase());
    }
}
