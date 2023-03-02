package com.yohwan.waiting.web.controller.member.dto;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String name;
    private MemberStatus memberStatus;
    private RoleType roleType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.name = member.getName();
        this.memberStatus = member.getMemberStatus();
        this.roleType = member.getRoleType();
        this.createdDate = member.getCreatedDate();
        this.modifiedDate = member.getModifiedDate();
    }
}
