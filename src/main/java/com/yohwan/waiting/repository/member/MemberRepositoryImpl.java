package com.yohwan.waiting.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.web.controller.member.dto.MemberResponseDto;
import com.yohwan.waiting.web.controller.member.dto.MemberSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.yohwan.waiting.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberResponseDto> search(MemberSearch memberSearch, Pageable pageable) {
        List<MemberResponseDto> content = queryFactory.select(Projections.constructor(MemberResponseDto.class, member))
                .from(member)
                .where(
                        keywordEq(memberSearch.getKeyword()),
                        roleTypeEq(memberSearch.getRoleType()),
                        usernameEq(memberSearch.getUsername()),
                        memberStatusEq(memberSearch.getMemberStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(member.count())
                .from(member)
                .where(
                        keywordEq(memberSearch.getKeyword()),
                        roleTypeEq(memberSearch.getRoleType())
                );

        return PageableExecutionUtils.getPage(content, pageable,count::fetchOne);
    }

    private BooleanExpression keywordEq(String keyword) {
        return StringUtils.hasText(keyword) ? member.name.contains(keyword).or(member.username.contains(keyword)) : null;
    }

    private BooleanExpression roleTypeEq(RoleType roleType) {
        return roleType != null ? member.roleType.eq(roleType) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression memberStatusEq(MemberStatus memberStatus) {
        return memberStatus != null ? member.memberStatus.eq(memberStatus) : null;
    }
}
