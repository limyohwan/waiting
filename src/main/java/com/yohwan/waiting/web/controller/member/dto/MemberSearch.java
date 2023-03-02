package com.yohwan.waiting.web.controller.member.dto;

import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSearch {
    private String keyword;
    private RoleType roleType;
    private String username;
    private MemberStatus memberStatus;
}
