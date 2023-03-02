package com.yohwan.waiting.web.controller.member;

import com.yohwan.waiting.security.auth.PrincipalDetails;
import com.yohwan.waiting.service.MemberService;
import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.yohwan.waiting.web.controller.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/api/members")
    public ApiResponseDto<Page<MemberResponseDto>> searchMember(MemberSearch memberSearch, Pageable pageable){
        return new ApiResponseDto(HttpStatus.OK, memberService.search(memberSearch, pageable));
    }

    @GetMapping("/api/members/me")
    public ApiResponseDto<MemberResponseDto> findMe(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiResponseDto(HttpStatus.OK, memberService.findById(principalDetails.getMember().getId()));
    }

    @GetMapping("/api/members/{id}")
    public ApiResponseDto<MemberResponseDto> findById(@PathVariable Long id){
        return new ApiResponseDto(HttpStatus.OK, memberService.findById(id));
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("/api/members")
    public ApiResponseDto<Long> save(@RequestBody @Valid MemberSaveRequestDto memberSaveRequestDto){
        return new ApiResponseDto(HttpStatus.OK, memberService.save(memberSaveRequestDto));
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("/api/members/{id}")
    public ApiResponseDto<Long> update(@PathVariable Long id, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto){
        return new ApiResponseDto(HttpStatus.OK, memberService.update(id, memberUpdateRequestDto));
    }

    @Secured("ROLE_MANAGER")
    @PostMapping("/api/members/{id}/password")
    public ApiResponseDto<Long> changePassword(@PathVariable Long id, @RequestBody @Valid MemberPasswordRequestDto memberPasswordRequestDto){
        return new ApiResponseDto(HttpStatus.OK, memberService.changePassword(id, memberPasswordRequestDto));
    }

    @Secured("ROLE_MANAGER")
    @DeleteMapping("/api/members/{id}")
    public ApiResponseDto<Long> delete(@PathVariable Long id){
        return new ApiResponseDto(HttpStatus.OK, memberService.deleteById(id));
    }
}