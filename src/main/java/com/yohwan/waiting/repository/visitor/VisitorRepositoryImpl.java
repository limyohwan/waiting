package com.yohwan.waiting.repository.visitor;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorResponseDto;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.yohwan.waiting.domain.visitor.QVisitor.visitor;
import static com.yohwan.waiting.domain.member.QMember.member;


@RequiredArgsConstructor
public class VisitorRepositoryImpl implements VisitorRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<VisitorResponseDto> search(VisitorSearch visitorSearch, Pageable pageable) {
        List<VisitorResponseDto> content = queryFactory
                .select(Projections.constructor(VisitorResponseDto.class, visitor))
                .from(visitor)
                .leftJoin(visitor.wizardMember, member).fetchJoin()
                .leftJoin(visitor.salesMember, member).fetchJoin()
                .where(
                        keywordContains(visitorSearch.getKeyword()),
                        dateBetween(visitorSearch.getStartDateTime(), visitorSearch.getEndDateTime()),
                        visitorStatusEq(visitorSearch.getVisitorStatus()),
                        memberEq(visitorSearch.getMemberId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
//                        orderByVisitorStatus(),
//                        visitor.createdDate.asc()
                        visitorSort(pageable)
                )
                .fetch();

        JPAQuery<Long> count = queryFactory.select(visitor.count())
                .from(visitor)
                .where(
                        keywordContains(visitorSearch.getKeyword()),
                        dateBetween(visitorSearch.getStartDateTime(), visitorSearch.getEndDateTime()),
                        visitorStatusEq(visitorSearch.getVisitorStatus()),
                        memberEq(visitorSearch.getMemberId())
                );

        return PageableExecutionUtils.getPage(content, pageable,count::fetchOne);
    }

    @Override
    public List<Visitor> searchForExcel(VisitorSearch visitorSearch) {
        return queryFactory.selectFrom(visitor)
                .leftJoin(visitor.wizardMember, member).fetchJoin()
                .leftJoin(visitor.salesMember, member).fetchJoin()
                .leftJoin(visitor.messageMember, member).fetchJoin()
                .where(
                        keywordContains(visitorSearch.getKeyword()),
                        dateBetween(visitorSearch.getStartDateTime(), visitorSearch.getEndDateTime()),
                        visitorStatusEq(visitorSearch.getVisitorStatus()),
                        memberEq(visitorSearch.getMemberId())
                )
                .fetch();
    }
    
    private OrderSpecifier<?> visitorSort(Pageable pageable){
        if(!pageable.getSort().isEmpty()){
            for(Sort.Order order : pageable.getSort()){
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch(order.getProperty()){
                    case "createdDate":
                        return new OrderSpecifier(direction, visitor.createdDate);
                    default:
                        return new OrderSpecifier(Order.ASC, visitor.createdDate);
                }
            }
        }
        return new OrderSpecifier(Order.ASC, visitor.createdDate);
    }

    private OrderSpecifier<?> orderByVisitorStatus() {
        return Expressions.stringTemplate(
                    "FIELD({0}, {1}, {2}, {3}, {4}, {5})",
                        visitor.visitorStatus,
                        VisitorStatus.WAIT.name(),
                        VisitorStatus.PREVIEW.name(),
                        VisitorStatus.CONSULT.name(),
                        VisitorStatus.END.name(),
                        VisitorStatus.DENY.name()
                )
                .asc();
    }

    private BooleanExpression memberEq(Long memberId) {
        return memberId != null ? visitor.wizardMember.id.eq(memberId).or(visitor.salesMember.id.eq(memberId)) : null;
    }

    private BooleanExpression visitorStatusEq(VisitorStatus visitorStatus) {
        return visitorStatus !=null ? visitor.visitorStatus.eq(visitorStatus) : null;
    }

    private BooleanExpression dateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return visitor.createdDate.between(startDateTime, endDateTime);
    }

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? visitor.name.contains(keyword).or(visitor.phoneNumber.substring(visitor.phoneNumber.length().add(-4),visitor.phoneNumber.length()).contains(keyword)) : null;
    }

}
