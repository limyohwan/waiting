package com.yohwan.waiting.service;

import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.repository.visitor.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class VisitorDeleteScheduler {
    private final VisitorRepository visitorRepository;

    @Scheduled(cron = "0  0  5  *  *  *")
    public void deleteVisitorWithoutMarketingInfo(){
        LocalDateTime findDateTime = findDateCustomDaysAgo(30);
        List<Visitor> visitors = visitorRepository.findByCreatedDateBeforeAndIsEnabledMarketingInfo(findDateTime, false);
        List<Long> ids = visitors.stream().map(v -> v.getId()).collect(Collectors.toList());
        visitorRepository.updateAllByIdInQuery(ids);
    }

    @Scheduled(cron = "0  30  5  *  *  *")
    public void deleteVisitorWithMarketingInfo(){
        LocalDateTime findDateTime = findDateCustomDaysAgo(365);
        List<Visitor> visitors = visitorRepository.findByCreatedDateBeforeAndIsEnabledMarketingInfo(findDateTime, true);
        List<Long> ids = visitors.stream().map(v -> v.getId()).collect(Collectors.toList());
        visitorRepository.updateAllByIdInQuery(ids);
    }

//    @Scheduled(cron = "0 0/5 *  *  *  *")
//    public void deleteVisitorWithoutMarketingInfoTest(){
//        LocalDateTime findDateTime = findDateCustomDaysAgo(5);
//        List<Visitor> visitors = visitorRepository.findByCreatedDateBeforeAndIsEnabledMarketingInfo(findDateTime, false);
//        log.debug(String.valueOf(findDateTime));
//        log.debug("[{}]삭제될 방문자 수 : {}", findDateTime, visitors.size());
//        for(Visitor v  : visitors){
//            log.debug("[{}]delete member : {} ",v.getCreatedDate(), v.getName());
//        }
//        List<Long> ids = visitors.stream().map(v -> v.getId()).collect(Collectors.toList());
//        visitorRepository.deleteAllByIdInQuery(ids);
//        log.debug("삭제될 방문자 아이디 수 : {}", ids.size());
//    }

    private LocalDateTime findDateCustomDaysAgo(int number) {
        LocalDateTime findDateTime = LocalDateTime.now().minusDays(number);
        return LocalDateTime.of(findDateTime.getYear(),findDateTime.getMonth(), findDateTime.getDayOfMonth(),0,0,0);
    }
}
