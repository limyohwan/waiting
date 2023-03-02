package com.yohwan.waiting.repository.member;

import com.yohwan.waiting.web.controller.member.dto.MemberResponseDto;
import com.yohwan.waiting.web.controller.member.dto.MemberSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<MemberResponseDto> search(MemberSearch memberSearch, Pageable pageable);
}
