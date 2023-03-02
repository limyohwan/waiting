package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.repository.member.MemberRepository;

import com.yohwan.waiting.web.exception.custom.MemberException;
import com.yohwan.waiting.web.controller.member.dto.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 멤버등록테스트() {

        MemberSaveRequestDto member1 = new MemberSaveRequestDto();
        member1.setUsername("yhlim123");
        member1.setPassword("1234");

        Long savedMember1 = memberService.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(savedMember1).get();
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member1.getUsername());
    }

    @Test(expected = MemberException.class)
    public void 중복멤버테스트(){
        MemberSaveRequestDto member1 = new MemberSaveRequestDto();
        member1.setUsername("yhlim123");
        member1.setPassword("1234");

        MemberSaveRequestDto member2 = new MemberSaveRequestDto();
        member2.setUsername("yhlim123");
        member2.setPassword("1234");

        Long savedMember1 = memberService.save(member1);
        em.flush();
        em.clear();

        Long savedMember2 = memberService.save(member2);
    }

    @Test(expected = MemberException.class)
    public void 등록되지않은멤버찾기테스트() {

        MemberSaveRequestDto member1 = new MemberSaveRequestDto();
        member1.setUsername("yhlim123");
        member1.setPassword("1234");

        Long savedMember1 = memberService.save(member1);
        em.flush();
        em.clear();

        memberService.findById(10L);
    }

    @Test(expected = MemberException.class)
    public void 멤버삭제테스트() {

        MemberSaveRequestDto member1 = new MemberSaveRequestDto();
        member1.setUsername("yhlim123");
        member1.setPassword("1234");

        Long savedMember1 = memberService.save(member1);
        em.flush();
        em.clear();

        memberService.deleteById(savedMember1);
        em.flush();
        em.clear();

        memberService.findById(savedMember1);
    }

    @Test
    public void 멤버수정테스트() {

        MemberSaveRequestDto member1 = new MemberSaveRequestDto();
        member1.setUsername("yhlim123");
        member1.setPassword("1234");
        member1.setName("임요환");
        member1.setMemberStatus(MemberStatus.VALID);
        member1.setRoleType(RoleType.ROLE_WIZARD);

        Long savedMember1 = memberService.save(member1);
        em.flush();
        em.clear();

        MemberUpdateRequestDto memberUpdateRequestDto = new MemberUpdateRequestDto();
        memberUpdateRequestDto.setName("이천수");
        memberUpdateRequestDto.setMemberStatus(MemberStatus.VALID);
        memberUpdateRequestDto.setRoleType(RoleType.ROLE_MANAGER);
        memberService.update(savedMember1, memberUpdateRequestDto);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(savedMember1).get();
        Assertions.assertThat(findMember.getName()).isEqualTo("이천수");
        Assertions.assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.VALID);
        Assertions.assertThat(findMember.getRoleType()).isEqualTo(RoleType.ROLE_MANAGER);
    }

    @Test
    public void 멤버비밀번호변경테스트(){
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setUsername("yhlim123");
        memberSaveRequestDto.setPassword("1234");
        memberSaveRequestDto.setName("임요환");
        memberSaveRequestDto.setMemberStatus(MemberStatus.VALID);
        memberSaveRequestDto.setRoleType(RoleType.ROLE_WIZARD);

        Long savedMember1 = memberService.save(memberSaveRequestDto);
        Member member1 = memberRepository.findById(savedMember1).get();
        String beforPassword = member1.getPassword();

        em.flush();
        em.clear();
        MemberPasswordRequestDto memberPasswordRequestDto = new MemberPasswordRequestDto();
        memberPasswordRequestDto.setPassword("12345");
        memberService.changePassword(savedMember1, memberPasswordRequestDto);

        em.flush();
        em.clear();

        Member member2 = memberRepository.findById(savedMember1).get();
        String afterPassowrd =member2.getPassword();
        Assertions.assertThat(beforPassword).isNotEqualTo(afterPassowrd);


    }

    @Test
    public void 키워드검색(){

        MemberSaveRequestDto member1 = new MemberSaveRequestDto();
        member1.setUsername("yhlim123");
        member1.setName("임요환");
        member1.setRoleType(RoleType.ROLE_WIZARD);
        member1.setPassword("1234");

        MemberSaveRequestDto member2 = new MemberSaveRequestDto();
        member2.setUsername("yhlim1");
        member2.setName("임지훈");
        member2.setRoleType(RoleType.ROLE_MANAGER);
        member2.setPassword("1234");

        memberService.save(member1);
        memberService.save(member2);

        em.flush();
        em.clear();
        
        MemberSearch memberSearch = new MemberSearch();
        memberSearch.setKeyword("yh");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MemberResponseDto> result = memberRepository.search(memberSearch, pageRequest);

        for(MemberResponseDto v : result.getContent()){
            System.out.println("v = " + v);
        }

        Assertions.assertThat(result.getContent().size()).isEqualTo(2);
    }
}