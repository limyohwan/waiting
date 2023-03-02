package com.yohwan.waiting.repository.visitor;

import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorCurrentAllResponseDto;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorSaveRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long>, VisitorRepositoryCustom {

    Long countByVisitorStatusAndCreatedDateBetween(VisitorStatus visitorStatus,LocalDateTime startDateTime, LocalDateTime endDateTime);
    Long countByCreatedDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    //    List<Visitor> findByCreatedDateBeforeAndIsEnabledMarketingInfo(LocalDateTime createdDate, boolean isEnabledMarketingInfo);
    @Query("select v from Visitor v where v.name != '***' and v.createdDate < :createdDate and v.isEnabledMarketingInfo = :isEnabledMarketingInfo")
    List<Visitor> findByCreatedDateBeforeAndIsEnabledMarketingInfo(@Param("createdDate") LocalDateTime createdDate, @Param("isEnabledMarketingInfo") boolean isEnabledMarketingInfo);
    @Query("select new com.yohwan.waiting.web.controller.visitor.dto.VisitorCurrentAllResponseDto(v.visitorStatus, COUNT(v.visitorStatus)) from Visitor v where v.createdDate between :startDateTime and :endDateTime group by v.visitorStatus")
    List<VisitorCurrentAllResponseDto> countCurrentTotalByVisitorStatus(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
    @Modifying
    @Transactional
    @Query("delete from Visitor v where v.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("update Visitor v set v.name = '***', v.phoneNumber = '***********' where v.id in :ids")
    void updateAllByIdInQuery(@Param("ids") List<Long> ids);

    /*
    * insert native query
    */
    @Modifying
    @Query(value = "insert into visitor (created_date, modified_date, age, gender, is_enabled_marketing_info, is_enabled_personal_info, is_sent_message, name, people_number, phone_number, visitor_status, visitor_type, wait_number) " +
            "values (now(), now(), :#{#visitor.age}, :#{#visitor.gender}, :#{#visitor.isEnabledMarketingInfo}, :#{#visitor.isEnabledPersonalInfo}, :isSentMessage, :#{#visitor.name}, :#{#visitor.peopleNumber}, :#{#visitor.phoneNumber}, :visitorStatus, :visitorType, (select count(*) + 1 from visitor ALIAS_FOR_SUBQUERY where created_date between :startDateTime and :endDateTime))"
            , nativeQuery = true)
    Integer saveVisitor(@Param("visitor") VisitorSaveRequestDto visitorSaveRequestDto
            , @Param("isSentMessage") Boolean isSentMessage
            , @Param("visitorStatus") String visitorStatus
            , @Param("visitorType") String visitorType
            , @Param("startDateTime") LocalDateTime startDateTime
            , @Param("endDateTime") LocalDateTime endDateTime);
}
