package com.yohwan.waiting.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MemberStatus {
    VALID("활성"),
    INVALID("비활성");

    private final String title;
    MemberStatus(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    @JsonCreator
    public static MemberStatus from(String s) {
        return MemberStatus.valueOf(s.toUpperCase());
    }
}
