package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.system.SystemSetting;
import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import com.yohwan.waiting.repository.SystemSettingRepository;
import com.yohwan.waiting.repository.member.MemberRepository;
import com.yohwan.waiting.repository.visitor.VisitorRepository;
import com.yohwan.waiting.web.controller.common.dto.CodeDto;
import com.yohwan.waiting.web.controller.visitor.dto.*;
import com.yohwan.waiting.web.exception.custom.MemberException;
import com.yohwan.waiting.web.exception.custom.VisitorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final MemberRepository memberRepository;
    private final MessageService messageService;
    private final SystemSettingRepository systemSettingRepository;

    public synchronized Long saveSync(VisitorSaveRequestDto visitorSaveRequestDto){
        return save(visitorSaveRequestDto);
    }
    @Transactional
    public Long save(VisitorSaveRequestDto visitorSaveRequestDto){
        Long currentCount = countCurrent() + 1;
        Visitor visitor = Visitor.builder()
                .name(visitorSaveRequestDto.getName())
                .phoneNumber(visitorSaveRequestDto.getPhoneNumber())
                .peopleNumber(visitorSaveRequestDto.getPeopleNumber())
                .visitorType(visitorSaveRequestDto.getVisitorType())
                .age(visitorSaveRequestDto.getAge())
                .gender(visitorSaveRequestDto.getGender())
                .visitorStatus(VisitorStatus.WAIT)
                .isEnabledPersonalInfo(visitorSaveRequestDto.getIsEnabledPersonalInfo())
                .isEnabledMarketingInfo(visitorSaveRequestDto.getIsEnabledMarketingInfo())
                .isSentMessage(false)
                .waitNumber(currentCount)
                .build();
        Long id = visitorRepository.save(visitor).getId();
        Long currentWaitCount = countCurrentWait();
        Integer expectedWaitTime = getExpectedWaitTime();
        sendSaveCompleteMessage(visitorSaveRequestDto.getPhoneNumber(), visitor.getWaitNumber(), currentWaitCount, expectedWaitTime);

        return id;
    }

    private Long countCurrent(){
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        return visitorRepository.countByCreatedDateBetween(startDateTime, endDateTime);
    }

    private Integer getExpectedWaitTime() {
        List<SystemSetting> result = systemSettingRepository.findAll();
        SystemSetting systemSetting = result.size() != 0 ? result.get(0) : null;
        return systemSetting != null ? systemSetting.getExpectedWaitTime() : 1;
    }

    private void sendSaveCompleteMessage(String phoneNumber, Long waitNumber, Long currentWaitCount, Integer expectedWaitTime) {
        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{number}", phoneNumber.substring(phoneNumber.length() - 4));
        variables.put("#{waitNumber}", String.valueOf(waitNumber));
        variables.put("#{countCurrentWait}", String.valueOf(currentWaitCount));
        variables.put("#{expectedWaitTime}", String.valueOf(expectedWaitTime * currentWaitCount));
        messageService.sendMessage(phoneNumber, 1, variables);
    }
    @Transactional
    public Long assignMember(Long id, VisitorUpdateRequestDto visitorUpdateRequestDto) {
        Visitor visitor = validateVisitorById(id);

        Member member = validateMemberById(visitorUpdateRequestDto.getMemberId());

        assignMemberToVisitor(visitorUpdateRequestDto, visitor, member);

        return id;
    }

    private Visitor validateVisitorById(Long id) {
        return visitorRepository.findById(id)
                .orElseThrow(() -> new VisitorException("방문객 id가 옳바르지않습니다"));
    }

    private Member validateMemberById(Long id) {
        return id != null ? memberRepository.findById(id)
                .orElseThrow(() -> new MemberException("담당자 id가 옳바르지않습니다")) : null;
    }

    private void assignMemberToVisitor(VisitorUpdateRequestDto visitorUpdateRequestDto, Visitor visitor, Member member) {
        if(member == null){
            visitor.deleteAssignedMember(visitorUpdateRequestDto.getRoleType());
        }else{
            visitor.assignMember(member);
        }
    }
    @Transactional
    public Long changeVisitorStatus(Long id, VisitorUpdateRequestDto visitorUpdateRequestDto) {
        Visitor visitor = validateVisitorById(id);

        visitor.changeVisitorStatus(visitorUpdateRequestDto.getVisitorStatus());

        checkAssignedMember(visitor);
        //2차알림 로직
        sendVisitAlarmMessage(visitorUpdateRequestDto, visitor);

        return id;
    }

    private void checkAssignedMember(Visitor visitor) {
        if(visitor.getPartMember() == null && visitor.getSalesMember() ==null){
            throw new MemberException("담당자를 설정해주세요");
        }
    }

    private void sendVisitAlarmMessage(VisitorUpdateRequestDto visitorUpdateRequestDto, Visitor visitor) {
        if(visitorUpdateRequestDto.getVisitorStatus() == VisitorStatus.PREVIEW && !visitor.isSentMessage()){
            Member member = visitor.getPartMember() != null ? visitor.getPartMember() : visitor.getSalesMember();
            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{number}", visitor.getPhoneNumber().substring(visitor.getPhoneNumber().length() - 4));
            variables.put("#{waitNumber}", String.valueOf(visitor.getWaitNumber()));
            messageService.sendMessage(visitor.getPhoneNumber(), 2, variables);
            visitor.sendMessageSuccess(member);
        }
    }

    @Transactional(readOnly = true)
    public Long countCurrentWait(){
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        return visitorRepository.countByVisitorStatusAndCreatedDateBetween(VisitorStatus.WAIT, startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public List<VisitorCurrentAllResponseDto> countCurrentVisitorStatues(List<CodeDto> visitorStatuses) {
        List<VisitorCurrentAllResponseDto> visitorCurrentAllResponseDtos = countCurrentAll();
        return setNullToZeroCount(visitorStatuses, visitorCurrentAllResponseDtos);
    }

    private List<VisitorCurrentAllResponseDto> setNullToZeroCount(List<CodeDto> visitorStatuses, List<VisitorCurrentAllResponseDto> visitorCurrentAllResponseDtos) {
        List<VisitorCurrentAllResponseDto> result = new ArrayList<>();
        for(CodeDto cd: visitorStatuses){
            boolean createNewDto = true;
            for(VisitorCurrentAllResponseDto vcar : visitorCurrentAllResponseDtos){
                if(vcar.getCode().name().equals(cd.getCode())){
                    result.add(new VisitorCurrentAllResponseDto(vcar.getCode(), vcar.getCount()));
                    createNewDto = false;
                    break;
                }
            }
            if(createNewDto){
                result.add(new VisitorCurrentAllResponseDto(VisitorStatus.valueOf(cd.getCode()), 0L));
            }
        }
        return result;
    }

    private List<VisitorCurrentAllResponseDto> countCurrentAll(){
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        return visitorRepository.countCurrentTotalByVisitorStatus(startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public Page<VisitorResponseDto> search(VisitorSearch visitorSearch, Pageable pageable) {
        return visitorRepository.search(visitorSearch, pageable);
    }

    @Transactional(readOnly = true)
    public List<Visitor> searchForExcel(VisitorSearch visitorSearch) {
        return visitorRepository.searchForExcel(visitorSearch);
    }


    /*
     *  primary key 못구함
     */
    private Long saveNotUsed(VisitorSaveRequestDto visitorSaveRequestDto){
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        Long currentWaitCount = countCurrentWait();
        Integer id = visitorRepository.saveVisitor(visitorSaveRequestDto
                ,false
                ,VisitorStatus.WAIT.name()
                ,visitorSaveRequestDto.getVisitorType().name()
                ,startDateTime
                ,endDateTime);
        Integer expectedWaitTime = getExpectedWaitTime();
//        sendSaveCompleteMessage(visitorSaveRequestDto.getPhoneNumber(), visitor.getWaitNumber(), currentWaitCount, expectedWaitTime);

        return Long.valueOf(id);
    }
}
