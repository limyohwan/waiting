package com.yohwan.waiting.web.controller.visitor;

import com.yohwan.waiting.service.CodeService;
import com.yohwan.waiting.service.SseService;
import com.yohwan.waiting.service.VisitorService;
import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.yohwan.waiting.web.controller.visitor.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class VisitorController {
    private final VisitorService visitorService;
    private final SseService sseService;
    private final CodeService codeService;

    @GetMapping("/api/visitors")
    public ApiResponseDto<Page<VisitorResponseDto>> searchVisitor(@Valid VisitorSearch visitorSearch, @PageableDefault(size = 10, sort ="createdDate", direction = Sort.Direction.ASC)Pageable pageable){
        return new ApiResponseDto(HttpStatus.OK,visitorService.search(visitorSearch, pageable));
    }

    @PostMapping("/api/visitors")
    public ApiResponseDto<Long> save(@RequestBody @Valid VisitorSaveRequestDto visitorSaveRequestDto){
        Long visitorId = visitorService.saveSync(visitorSaveRequestDto);
        sseService.sendAllConnectedClient("visitorDetected");
        return new ApiResponseDto(HttpStatus.OK,visitorId);
    }

    @PostMapping("/api/visitors/{visitorId}/members")
    public ApiResponseDto<Long> assignMember(@PathVariable Long visitorId, @RequestBody VisitorUpdateRequestDto visitorUpdateRequestDto){
        return new ApiResponseDto(HttpStatus.OK,visitorService.assignMember(visitorId, visitorUpdateRequestDto));
    }

    @PostMapping("/api/visitors/{id}/visitor-status")
    public ApiResponseDto<Long> changeVisitorStatus(@PathVariable Long id, @RequestBody VisitorUpdateRequestDto visitorUpdateRequestDto){
        return new ApiResponseDto(HttpStatus.OK,visitorService.changeVisitorStatus(id, visitorUpdateRequestDto));
    }

    @GetMapping("/api/visitors/current")
    public ApiResponseDto<Long> countCurrentWait(){
        return new ApiResponseDto(HttpStatus.OK,visitorService.countCurrentWait());
    }

    @GetMapping("/api/visitors/current-all")
    public ApiResponseDto<List<VisitorCurrentAllResponseDto>> countCurrentAll(){
        return new ApiResponseDto(HttpStatus.OK,visitorService.countCurrentVisitorStatues(codeService.getVisitorStatus()));
    }
}
