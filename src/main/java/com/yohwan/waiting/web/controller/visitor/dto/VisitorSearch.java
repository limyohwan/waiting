package com.yohwan.waiting.web.controller.visitor.dto;

import com.yohwan.waiting.domain.visitor.VisitorStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VisitorSearch {
    @NotNull(message="시작시간을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull(message="종료시간을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;
    private String keyword;
    private VisitorStatus visitorStatus;
    private Long memberId;
}
