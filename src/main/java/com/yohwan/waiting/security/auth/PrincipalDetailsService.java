package com.yohwan.waiting.security.auth;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.repository.member.MemberRepository;
import com.yohwan.waiting.web.exception.custom.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsernameAndMemberStatus(username, MemberStatus.VALID)
                .orElseThrow(() -> new MemberException("등록정보를 확인해주세요"));

        AuthMember authMember = new AuthMember(member);
        return new PrincipalDetails(authMember);
    }
}
