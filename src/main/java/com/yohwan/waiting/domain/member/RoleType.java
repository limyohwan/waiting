package com.yohwan.waiting.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleType {
    ROLE_USER("고객화면전용"),
    ROLE_MANAGER("관리자"),
    ROLE_WIZARD("위자드"),
    ROLE_SALES("영업사원");

    private final String title;
    RoleType(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    @JsonCreator
    public static RoleType from(String s) {
        return RoleType.valueOf(s.toUpperCase());
    }
}
