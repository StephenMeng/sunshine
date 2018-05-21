package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.JedisConst;
import team.stephen.sunshine.model.common.Email;
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
        String address = "mengfansai147@163.com";
        String[] cc = {"1581284397@qq.com"};
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>大标题-h1</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");
        Email email = new Email();
        email.setFromAddress(address);
        email.setHtml(true);
        email.setToAddress(address);
        email.setCcAddress(cc);
        email.setSubject("测试：html邮件");
        email.setContent(sb.toString());
        mailService.sendMail(email);
    }

}
