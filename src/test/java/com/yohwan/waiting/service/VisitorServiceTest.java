package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import com.yohwan.waiting.domain.visitor.VisitorType;
import com.yohwan.waiting.repository.visitor.VisitorRepository;
import com.yohwan.waiting.web.exception.custom.MemberException;
import com.yohwan.waiting.web.exception.custom.VisitorException;
import com.yohwan.waiting.web.controller.member.dto.MemberSaveRequestDto;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorResponseDto;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorSaveRequestDto;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorSearch;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorUpdateRequestDto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class VisitorServiceTest {

    @Autowired
    VisitorService visitorService;
    @Autowired
    EntityManager em;
    @Autowired
    VisitorRepository visitorRepository;
    @Autowired
    MemberService memberService;

    private String phoneNumber = "01012345678";

    @Test
    public void 방문객등록(){
        String name = "임요환";
        int peopleNumber = 1;
        VisitorType visitorType = VisitorType.CONSULT_HOPE;
        int age = 20;
        String gender = "M";
        boolean isEnablePersonalInfo = true;
        boolean isEnabledMarketingInfo = true;
        VisitorSaveRequestDto visitorSaveRequestDto = getVisitorSaveRequestDto(name, phoneNumber, peopleNumber, visitorType, age, gender, isEnablePersonalInfo, isEnabledMarketingInfo);
        Long visitorId = visitorService.save(visitorSaveRequestDto);

        em.flush();
        em.clear();

        Visitor visitor = visitorRepository.findById(visitorId).get();

        Assertions.assertThat(visitor.getName()).isEqualTo(name);
        Assertions.assertThat(visitor.getPeopleNumber()).isEqualTo(peopleNumber);
        Assertions.assertThat(visitor.getPhoneNumber()).isEqualTo(phoneNumber);
        Assertions.assertThat(visitor.getVisitorStatus()).isEqualTo(VisitorStatus.WAIT);
        Assertions.assertThat(visitor.getGender()).isEqualTo("M");
    }
    private Long saveMember(String username, String password, String name, RoleType roleType){
        MemberSaveRequestDto requestDto = new MemberSaveRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);
        requestDto.setName(name);
        requestDto.setRoleType(roleType);
        requestDto.setMemberStatus(MemberStatus.VALID);
        return memberService.save(requestDto);
    }

    public VisitorSaveRequestDto getVisitorSaveRequestDto(String name, String phoneNumber, int peopleNumber, VisitorType visitorType, int age, String gender, boolean isEnablePersonalInfo, boolean isEnabledMarketingInfo){
        VisitorSaveRequestDto visitorSaveRequestDto = new VisitorSaveRequestDto();
        visitorSaveRequestDto.setName(name);
        visitorSaveRequestDto.setPhoneNumber(phoneNumber);
        visitorSaveRequestDto.setPeopleNumber(peopleNumber);
        visitorSaveRequestDto.setVisitorType(visitorType);
        visitorSaveRequestDto.setAge(age);
        visitorSaveRequestDto.setGender(gender);
        visitorSaveRequestDto.setIsEnabledPersonalInfo(isEnablePersonalInfo);
        visitorSaveRequestDto.setIsEnabledMarketingInfo(isEnabledMarketingInfo);
        return visitorSaveRequestDto;
    }

    private Long saveVisitor(){
        String name = "임요환";
        int peopleNumber = 1;
        VisitorType visitorType = VisitorType.CONSULT_HOPE;
        int age = 20;
        String gender = "M";
        boolean isEnablePersonalInfo = true;
        boolean isEnabledMarketingInfo = true;

        VisitorSaveRequestDto visitorSaveRequestDto = getVisitorSaveRequestDto(name, phoneNumber, peopleNumber, visitorType, age, gender, isEnablePersonalInfo, isEnabledMarketingInfo);

        return visitorService.save(visitorSaveRequestDto);
    }

    @Test
    public void 방문객담당설정(){
        Long wizardMemberId = saveMember("yhlim", "1234", "손흥민", RoleType.ROLE_WIZARD);
        Long visitorId = saveVisitor();
        VisitorUpdateRequestDto visitorUpdateRequestDto = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto.setMemberId(wizardMemberId);
        visitorUpdateRequestDto.setRoleType(RoleType.ROLE_WIZARD);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto);

        em.flush();
        em.clear();

        Visitor visitor = visitorRepository.findById(visitorId).get();
        Assertions.assertThat(visitor.getWizardMember().getId()).isEqualTo(wizardMemberId);

        Long salesMemberId = saveMember("yhlim2", "1234", "이승우", RoleType.ROLE_SALES);
        VisitorUpdateRequestDto visitorUpdateRequestDto2 = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto2.setMemberId(salesMemberId);
        visitorUpdateRequestDto2.setRoleType(RoleType.ROLE_SALES);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto2);

        em.flush();
        em.clear();

        Visitor visitor2 = visitorRepository.findById(visitorId).get();
        Assertions.assertThat(visitor2.getSalesMember().getId()).isEqualTo(salesMemberId);

    }

    @Test(expected = VisitorException.class)
    public void 방문객담당설정방문객잘못입력시에러(){
        VisitorUpdateRequestDto visitorUpdateRequestDto = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto.setMemberId(1L);
        visitorUpdateRequestDto.setRoleType(RoleType.ROLE_SALES);
        visitorService.assignMember(1L, visitorUpdateRequestDto);
    }

    @Test(expected = MemberException.class)
    public void 방문객담당설정담당자잘못입력시에러(){
        Long visitorId = saveVisitor();
        VisitorUpdateRequestDto visitorUpdateRequestDto = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto.setMemberId(1L);
        visitorUpdateRequestDto.setRoleType(RoleType.ROLE_SALES);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto);
    }

    @Test
    public void 방문객담당설정후취소(){
        Long wizardMemberId = saveMember("yhlim", "1234", "손흥민", RoleType.ROLE_WIZARD);
        Long visitorId = saveVisitor();
        VisitorUpdateRequestDto visitorUpdateRequestDto = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto.setMemberId(wizardMemberId);
        visitorUpdateRequestDto.setRoleType(RoleType.ROLE_WIZARD);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto);

        em.flush();
        em.clear();

        Visitor visitor = visitorRepository.findById(visitorId).get();
        Assertions.assertThat(visitor.getWizardMember().getId()).isEqualTo(wizardMemberId);

        Long salesMemberId = saveMember("yhlim2", "1234", "이승우", RoleType.ROLE_SALES);
        VisitorUpdateRequestDto visitorUpdateRequestDto2 = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto2.setMemberId(salesMemberId);
        visitorUpdateRequestDto2.setRoleType(RoleType.ROLE_SALES);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto2);

        em.flush();
        em.clear();

        Visitor visitor2 = visitorRepository.findById(visitorId).get();
        Assertions.assertThat(visitor2.getSalesMember().getId()).isEqualTo(salesMemberId);

        em.flush();
        em.clear();


        VisitorUpdateRequestDto visitorUpdateRequestDto3 = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto3.setRoleType(RoleType.ROLE_SALES);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto3);

        em.flush();
        em.clear();

        Visitor visitor3 = visitorRepository.findById(visitorId).get();
        Assertions.assertThat(visitor3.getSalesMember()).isNull();


        em.flush();
        em.clear();


        VisitorUpdateRequestDto visitorUpdateRequestDto4 = new VisitorUpdateRequestDto();
        visitorUpdateRequestDto4.setRoleType(RoleType.ROLE_WIZARD);
        visitorService.assignMember(visitorId, visitorUpdateRequestDto4);

        em.flush();
        em.clear();

        Visitor visitor4 = visitorRepository.findById(visitorId).get();
        Assertions.assertThat(visitor4.getWizardMember()).isNull();
    }

    @Test
    public void 대기고객테스트아무도없을때() {
        Long count = visitorService.countCurrentWait();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void 대기고객50명있을때(){
        for(int i = 0; i < 100; i++){
            VisitorStatus status = VisitorStatus.DENY;
            if(i % 2 == 1){
                status = VisitorStatus.WAIT;
            }
            Visitor visitor = Visitor.builder()
                    .name("임요환"+i)
                    .phoneNumber(phoneNumber)
                    .peopleNumber(1)
                    .visitorType(VisitorType.CONSULT_HOPE)
                    .visitorStatus(status)
                    .isEnabledPersonalInfo(true)
                    .isEnabledMarketingInfo(true)
                    .isSentMessage(false)
                    .build();

            visitorRepository.save(visitor);
        }

        em.flush();
        em.clear();

        Long count = visitorService.countCurrentWait();
        Assertions.assertThat(count).isEqualTo(50);

    }

    @Test
    public void 페이징서치테스트(){
        for(int i = 0; i < 100; i++){
            VisitorStatus status = VisitorStatus.DENY;
            if(i % 2 == 1){
                status = VisitorStatus.WAIT;
            }
            Visitor visitor = Visitor.builder()
                    .name("임요환"+i)
                    .phoneNumber(phoneNumber)
                    .peopleNumber(1)
                    .visitorType(VisitorType.CONSULT_HOPE)
                    .visitorStatus(status)
                    .isEnabledPersonalInfo(true)
                    .isEnabledMarketingInfo(true)
                    .isSentMessage(false)
                    .build();

            visitorRepository.save(visitor);
        }

        em.flush();
        em.clear();

        VisitorSearch visitorSearch = new VisitorSearch();

        visitorSearch.setStartDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0)));
        visitorSearch.setEndDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)));
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<VisitorResponseDto> result = visitorRepository.search(visitorSearch, pageRequest);

        for(VisitorResponseDto v : result.getContent()){
            System.out.println("v = " + v);
        }

        Assertions.assertThat(result.getContent().size()).isEqualTo(10);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(100);
    }

    @Test
    public void 엑셀서치테스트(){
        for(int i = 0; i < 100; i++){
            VisitorStatus status = VisitorStatus.DENY;
            if(i % 2 == 1){
                status = VisitorStatus.WAIT;
            }
            Visitor visitor = Visitor.builder()
                    .name("임요환"+i)
                    .phoneNumber(phoneNumber)
                    .peopleNumber(1)
                    .visitorType(VisitorType.CONSULT_HOPE)
                    .visitorStatus(status)
                    .isEnabledPersonalInfo(true)
                    .isEnabledMarketingInfo(true)
                    .isSentMessage(false)
                    .build();

            visitorRepository.save(visitor);
        }

        em.flush();
        em.clear();

        VisitorSearch visitorSearch = new VisitorSearch();
        visitorSearch.setStartDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0)));
        visitorSearch.setEndDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)));
        List<Visitor> result = visitorRepository.searchForExcel(visitorSearch);

        for(Visitor v : result){
            System.out.println("v = " + v);
        }

        Assertions.assertThat(result.size()).isEqualTo(100);
    }

    @Test
    public void 벌크삭제테스트(){
        for(int i = 0; i < 100; i++){
            VisitorStatus status = VisitorStatus.DENY;
            if(i % 2 == 1){
                status = VisitorStatus.WAIT;
            }
            Visitor visitor = Visitor.builder()
                    .name("임요환"+i)
                    .phoneNumber(phoneNumber)
                    .peopleNumber(1)
                    .visitorType(VisitorType.CONSULT_HOPE)
                    .visitorStatus(status)
                    .isEnabledPersonalInfo(true)
                    .isEnabledMarketingInfo(true)
                    .isSentMessage(false)
                    .build();

            visitorRepository.save(visitor);
        }

        em.flush();
        em.clear();

        VisitorSearch visitorSearch = new VisitorSearch();
        visitorSearch.setStartDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0)));
        visitorSearch.setEndDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)));
        List<Visitor> result = visitorRepository.searchForExcel(visitorSearch);

        em.flush();
        em.clear();

        List<Long> ids = result.stream().map(v -> v.getId()).collect(Collectors.toList());
        for (Long id : ids) {
            System.out.println("id = " + id);
        }
        visitorRepository.deleteAllByIdInQuery(ids);

        em.flush();
        em.clear();

        List<Visitor> result2 = visitorRepository.searchForExcel(visitorSearch);

        Assertions.assertThat(result2.size()).isEqualTo(0);
    }

    @Test
    public void 현재번호테스트(){
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        Long count = visitorRepository.countByCreatedDateBetween(startDateTime, endDateTime) + 1;

        Assertions.assertThat(count).isEqualTo(1);
    }
}