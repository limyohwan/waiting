package com.yohwan.waiting.repository.member;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByUsernameAndMemberStatus(String username, MemberStatus memberStatus);
}
