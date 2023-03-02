package com.yohwan.waiting.web.controller.member.dto;

import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class MemberSaveRequestDto {
    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "[a-zA-Z0-9]{4,20}", message="아이디는 영문,숫자(4~20자리)만 입력가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotNull(message = "멤버상태는 필수입니다.")
    private MemberStatus memberStatus;

    @NotNull(message = "권한은 필수입니다.")
    private RoleType roleType;
}
