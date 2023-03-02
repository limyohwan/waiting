package com.yohwan.waiting.repository.member;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.RoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원등록() {

        String username = "yhlim";
        String password = "password";

        memberRepository.save(Member.builder()
                .username(username)
                .password(password)
                .roleType(RoleType.ROLE_MANAGER)
                .build());

        List<Member> members = memberRepository.findAll();

        Member member = members.get(0);
        assertThat(member.getUsername()).isEqualTo(username);
        assertThat(member.getPassword()).isEqualTo(password);

    }
}
