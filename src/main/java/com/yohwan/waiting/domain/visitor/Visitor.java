package com.yohwan.waiting.domain.visitor;

import com.yohwan.waiting.domain.BaseEntity;
import com.yohwan.waiting.domain.member.Member;
import com.yohwan.waiting.domain.member.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Visitor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visitor_id")
    private Long id;
    private String name;
    private String phoneNumber;
    private Integer peopleNumber;
    @Enumerated(EnumType.STRING)
    private VisitorType visitorType;
    private Integer age;
    private String gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wizard_member_id")
    private Member wizardMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_member_id")
    private Member salesMember;
    @Enumerated(EnumType.STRING)
    private VisitorStatus visitorStatus;
    private boolean isEnabledPersonalInfo;
    private boolean isEnabledMarketingInfo;
    private boolean isSentMessage;
    private LocalDateTime messageDate;
    private Long waitNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_member_id")
    private Member  messageMember;

    @Builder
    public Visitor(String name, String phoneNumber, int peopleNumber, VisitorType visitorType, Integer age, String gender, VisitorStatus visitorStatus, Boolean isEnabledPersonalInfo, Boolean isEnabledMarketingInfo, Boolean isSentMessage, Long waitNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.peopleNumber = peopleNumber;
        this.visitorType = visitorType;
        this.age = age;
        this.gender = gender;
        this.visitorStatus = visitorStatus;
        this.isEnabledPersonalInfo = isEnabledPersonalInfo;
        this.isEnabledMarketingInfo = isEnabledMarketingInfo;
        this.isSentMessage = isSentMessage;
        this.waitNumber = waitNumber;
    }

    public void assignMember(Member member){
        if(member.getRoleType().equals(RoleType.ROLE_WIZARD)){
            this.wizardMember = member;
        }else if(member.getRoleType().equals(RoleType.ROLE_SALES)){
            this.salesMember = member;
        }
    }

    public void sendMessageSuccess(Member member){
        this.isSentMessage = true;
        this.messageDate = LocalDateTime.now();
        this.messageMember = member;
    }

    public void changeVisitorStatus(VisitorStatus visitorStatus){
        this.visitorStatus = visitorStatus;
    }

    public void deleteAssignedMember(RoleType roleType) {
        if(roleType.equals(RoleType.ROLE_WIZARD)){
            this.wizardMember = null;
        }else if(roleType.equals(RoleType.ROLE_SALES)){
            this.salesMember = null;
        }
    }
}
