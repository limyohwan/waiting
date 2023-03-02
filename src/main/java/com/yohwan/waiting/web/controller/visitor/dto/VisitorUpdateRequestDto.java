package com.yohwan.waiting.web.controller.visitor.dto;

import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisitorUpdateRequestDto {
    private Long memberId;
    private RoleType roleType;
    private VisitorStatus visitorStatus;
}
