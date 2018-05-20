package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.JedisConst;
import team.stephen.sunshine.service.common.MailService;
import team.stephen.sunshine.service.common.impl.JedisService;
import team.stephen.sunshine.util.LogRecod;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @Test
    public void testSendSimpleMail() {
        mailService.sendMail();
    }

}
