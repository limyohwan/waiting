package com.yohwan.waiting.domain.member;

import com.yohwan.waiting.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    public Member(String username, String password, RoleType roleType, String name, MemberStatus memberStatus) {
        this.username = username;
        this.password = password;
        this.roleType = roleType;
        this.name = name;
        this.memberStatus = memberStatus;
    }

    public void updateMember(RoleType roleType, String name, MemberStatus memberStatus){
        this.roleType = roleType;
        this.name = name;
        this.memberStatus = memberStatus;
    }

    public void changePassword(String password){
        this.password = password;
    }

}
