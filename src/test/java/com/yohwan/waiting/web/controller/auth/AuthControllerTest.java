package com.yohwan.waiting.web.controller.auth;


import com.yohwan.waiting.domain.member.MemberStatus;
import com.yohwan.waiting.domain.member.RoleType;
import com.yohwan.waiting.service.AuthService;
import com.yohwan.waiting.service.MemberService;
import com.yohwan.waiting.web.controller.auth.dto.AuthLoginDto;
import com.yohwan.waiting.web.controller.auth.dto.JwtDto;
import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.yohwan.waiting.web.controller.member.dto.MemberSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberService memberService;

    @Test
    public void loginTest() throws Exception {
        String username = "yhlim";
        String password = "test1234";
        String name = "임요환";
        MemberStatus memberStatus = MemberStatus.VALID;
        RoleType roleType = RoleType.ROLE_MANAGER;

        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto(username, password, name, memberStatus, roleType);

        memberService.save(memberSaveRequestDto);

        String url = "http://localhost:" + port + "/api/auth/login";

        AuthLoginDto authLoginDto = new AuthLoginDto(username, password);

        ResponseEntity<ApiResponseDto> result = restTemplate.postForEntity(url, authLoginDto, ApiResponseDto.class);

        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(result.getBody().getData());
    }

}