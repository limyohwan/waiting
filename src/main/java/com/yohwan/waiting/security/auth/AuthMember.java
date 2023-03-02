package com.yohwan.waiting.security.auth;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.RoleType;
import lombok.Data;

@Data
public class AuthMember {
    private Long id;
    private String username;
    private String password;
    private String name;
    private RoleType roleType;

    public AuthMember(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.name = member.getName();
        this.roleType = member.getRoleType();
    }
}
