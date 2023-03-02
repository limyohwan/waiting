package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import com.yohwan.waiting.domain.visitor.VisitorType;
import com.yohwan.waiting.web.controller.common.dto.CodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeService {
    private static final List<CodeDto> roleTypes = new ArrayList<>();
    private static final List<CodeDto> visitorStatuses = new ArrayList<>();
    private static final List<CodeDto> visitorTypes = new ArrayList<>();
    private static final List<CodeDto> memberStatuses = new ArrayList<>();


    public List<CodeDto> getRoleTypes(){
        if(roleTypes.isEmpty()){
            Arrays.stream(RoleType.values()).map(rt -> roleTypes.add(new CodeDto(rt.name(), rt.getTitle()))).collect(Collectors.toList());
        }
        return roleTypes;
    }

    public List<CodeDto> getVisitorStatus() {
        if(visitorStatuses.isEmpty()){
            Arrays.stream(VisitorStatus.values()).map(vs -> visitorStatuses.add(new CodeDto(vs.name(), vs.getTitle()))).collect(Collectors.toList());
        }
        return visitorStatuses;
    }

    public List<CodeDto> getVisitorType() {
        if(visitorTypes.isEmpty()){
            Arrays.stream(VisitorType.values()).map(vt -> visitorTypes.add(new CodeDto(vt.name(), vt.getTitle()))).collect(Collectors.toList());
        }
        return visitorTypes;
    }

    public List<CodeDto> getMemberStatus() {
        if(memberStatuses.isEmpty()){
            Arrays.stream(MemberStatus.values()).map(ms -> memberStatuses.add(new CodeDto(ms.name(), ms.getTitle()))).collect(Collectors.toList());
        }
        return memberStatuses;
    }
}
