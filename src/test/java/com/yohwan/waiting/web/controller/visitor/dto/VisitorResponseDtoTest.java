package com.yohwan.waiting.web.controller.visitor.dto;

import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.domain.visitor.VisitorType;
import com.yohwan.waiting.repository.visitor.VisitorRepository;
import com.yohwan.waiting.service.VisitorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class VisitorResponseDtoTest {

    @Autowired
    VisitorService visitorService;

    @Autowired
    VisitorRepository visitorRepository;

    @Test
    public void 마스킹테스트11자리(){
        Long visitorId = saveVisitor("01012345678");

        Visitor visitor = visitorRepository.findById(visitorId).get();

        VisitorResponseDto visitorResponseDto = new VisitorResponseDto(visitor);
        System.out.println(visitorResponseDto.getPhoneNumber());
        assertThat(visitorResponseDto.getPhoneNumber()).isEqualTo("010****5678");

    }

    @Test
    public void 마스킹테스트10자리(){
        Long visitorId = saveVisitor("0101234567");

        Visitor visitor = visitorRepository.findById(visitorId).get();

        VisitorResponseDto visitorResponseDto = new VisitorResponseDto(visitor);
        System.out.println(visitorResponseDto.getPhoneNumber());
        assertThat(visitorResponseDto.getPhoneNumber()).isEqualTo("010***4567");
    }

    private Long saveVisitor(String phoneNumber){
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
}