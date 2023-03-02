package com.yohwan.waiting.web.controller.visitor.dto;

import com.yohwan.waiting.domain.visitor.VisitorStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisitorCurrentAllResponseDto {
    private VisitorStatus code;
    private String name;
    private Long count;

    public VisitorCurrentAllResponseDto(VisitorStatus visitorStatus, Long count) {
        this.code = visitorStatus;
        this.name = visitorStatus.getTitle();
        this.count = count;
    }
}
