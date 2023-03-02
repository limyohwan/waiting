package com.yohwan.waiting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class VisitorDeleteSchedulerTest {

    @Autowired
    VisitorDeleteScheduler visitorDeleteScheduler;

    @Autowired
    EntityManager em;
    @Test
    public void 스케쥴러업데이트테스트마케팅동의(){
        visitorDeleteScheduler.deleteVisitorWithMarketingInfo();
    }

    @Test
    public void 스케쥴러업데이트테스트마케팅미동의(){
        visitorDeleteScheduler.deleteVisitorWithoutMarketingInfo();
    }

}