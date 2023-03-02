package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.repository.member.MemberRepository;
import com.yohwan.waiting.web.controller.member.dto.*;
import com.yohwan.waiting.web.exception.custom.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(MemberSaveRequestDto memberSaveRequestDto){
        validateDuplicateMemberByUsername(memberSaveRequestDto.getUsername());

        String encPassword = passwordEncoder.encode(memberSaveRequestDto.getPassword());
        Member member = Member.builder()
                .username(memberSaveRequestDto.getUsername())
                .password(encPassword)
                .name(memberSaveRequestDto.getName())
                .memberStatus(memberSaveRequestDto.getMemberStatus())
                .roleType(memberSaveRequestDto.getRoleType())
                .build();

        return memberRepository.save(member).getId();
    }

    private void validateDuplicateMemberByUsername(String username) {
        memberRepository.findByUsername(username).ifPresent(m -> {
                throw new MemberException("이미존재하는 유저입니다");
            }
        );
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findById(Long id){
        Member member = validateMemberById(id);
        return new MemberResponseDto(member);
    }

    public Long deleteById(Long id){
        memberRepository.deleteById(id);
        return id;
    }

    public Long update(Long id, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = validateMemberById(id);
        member.updateMember(memberUpdateRequestDto.getRoleType(), memberUpdateRequestDto.getName(), memberUpdateRequestDto.getMemberStatus());
        return id;
    }

    public Long changePassword(Long id, MemberPasswordRequestDto memberPasswordRequestDto){
        Member member = validateMemberById(id);
        String encPassword = passwordEncoder.encode(memberPasswordRequestDto.getPassword());
        member.changePassword(encPassword);
        return id;
    }

    private Member validateMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberException("user가 등록되어 있지 않습니다. id : " + id));
    }

    @Transactional(readOnly = true)
    public Page<MemberResponseDto> search(MemberSearch memberSearch, Pageable pageable) {
        return memberRepository.search(memberSearch, pageable);
    }
}
