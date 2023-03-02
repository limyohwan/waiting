package com.yohwan.waiting.web.controller.visitor.dto;

import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.domain.visitor.VisitorStatus;
import com.yohwan.waiting.domain.visitor.VisitorType;
import com.yohwan.waiting.web.controller.member.dto.MemberResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VisitorResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private Integer peopleNumber;
    private VisitorType visitorType;
    private Integer age;
    private String gender;
    private MemberResponseDto wizardMember;
    private MemberResponseDto salesMember;
    private VisitorStatus visitorStatus;
    private Boolean isSentMessage;
    private LocalDateTime messageDate;
    private LocalDateTime createdDate;
    private Long waitNumber;

    public VisitorResponseDto(Visitor visitor){
        this.id = visitor.getId();
        this.name = visitor.getName();
        this.phoneNumber = maskPhoneNumber(visitor.getPhoneNumber());
        this.peopleNumber = visitor.getPeopleNumber();
        this.visitorType = visitor.getVisitorType();
        this.age = visitor.getAge();
        this.gender = visitor.getGender();
        if(visitor.getWizardMember() != null){
            this.wizardMember = new MemberResponseDto(visitor.getWizardMember());
        }
        if(visitor.getSalesMember() != null){
            this.salesMember = new MemberResponseDto(visitor.getSalesMember());
        }
        this.visitorStatus = visitor.getVisitorStatus();
        this.isSentMessage = visitor.isSentMessage();
        this.createdDate = visitor.getCreatedDate();
        this.messageDate = visitor.getMessageDate();
        this.waitNumber = visitor.getWaitNumber();
    }

    private String maskPhoneNumber(String phoneNumber){
        if(phoneNumber.length() == 10){
            phoneNumber = phoneNumber.substring(0,3) + "***" + phoneNumber.substring(6);
        }else if(phoneNumber.length() == 11){
            phoneNumber = phoneNumber.substring(0,3) + "****" + phoneNumber.substring(7);

        }
        return phoneNumber;
    }
}
