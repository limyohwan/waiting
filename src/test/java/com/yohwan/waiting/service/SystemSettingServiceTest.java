package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.system.SystemSetting;
import com.yohwan.waiting.repository.SystemSettingRepository;
import com.yohwan.waiting.web.controller.system.dto.SystemSettingResponseDto;
import com.yohwan.waiting.web.controller.system.dto.SystemSettingUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class SystemSettingServiceTest {
    @Autowired
    SystemSettingRepository systemSettingRepository;

    @Autowired
    SystemSettingService systemSettingService;

    @Autowired
    EntityManager em;

    @Test
    public void findSystemSetting() {
        SystemSetting systemSetting = SystemSetting.builder().expectedWaitTime(5).build();
        systemSettingRepository.save(systemSetting);
        em.flush();
        em.clear();

        SystemSettingResponseDto findSystemSetting = systemSettingService.findSystemSetting();

        Assertions.assertThat(findSystemSetting.getId()).isEqualTo(1L);
        Assertions.assertThat(findSystemSetting.getExpectedWaitTime()).isEqualTo(5);
    }

    @Test
    public void changeExpectedWaitTime() {
        SystemSetting systemSetting = SystemSetting.builder().expectedWaitTime(5).build();
        systemSettingRepository.save(systemSetting);
        em.flush();
        em.clear();

        SystemSettingUpdateRequestDto systemSettingUpdateRequestDto = new SystemSettingUpdateRequestDto();
        systemSettingUpdateRequestDto.setSystemSettingId(systemSetting.getId());
        systemSettingUpdateRequestDto.setExpectedWaitTime(10);
        systemSettingService.createOrChangeExpectedWaitTime(systemSettingUpdateRequestDto);

        em.flush();
        em.clear();

        SystemSettingResponseDto findSystemSetting = systemSettingService.findSystemSetting();

        Assertions.assertThat(findSystemSetting.getExpectedWaitTime()).isEqualTo(10);
    }

    @Test
    public void createSystemSetting() {
        SystemSettingUpdateRequestDto systemSettingUpdateRequestDto = new SystemSettingUpdateRequestDto();
        systemSettingUpdateRequestDto.setExpectedWaitTime(10);
        systemSettingService.createOrChangeExpectedWaitTime(systemSettingUpdateRequestDto);

        em.flush();
        em.clear();

        SystemSettingResponseDto findSystemSetting = systemSettingService.findSystemSetting();

        Assertions.assertThat(findSystemSetting.getExpectedWaitTime()).isEqualTo(10);
    }
}