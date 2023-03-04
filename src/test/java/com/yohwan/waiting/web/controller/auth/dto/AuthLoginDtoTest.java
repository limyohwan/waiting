package com.yohwan.waiting.web.controller.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AuthLoginDtoTest {

    @Test
    public void AuthLoginDtoGenerateTest(){
        String username = "yhlim";
        String password = "test1234";

        AuthLoginDto authLoginDto = new AuthLoginDto(username, password);

        assertThat(authLoginDto.getUsername()).isEqualTo(username);
        assertThat(authLoginDto.getPassword()).isEqualTo(password);
    }

}