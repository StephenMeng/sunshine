package team.stephen.sunshine.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.service.common.MailService;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public int sendMail() {
        String sender = "mengfansai147@163.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(sender); //自己给自己发送邮件
        message.setSubject("主题：测试简单邮件");
        message.setText("测试邮件内容");
        javaMailSender.send(message);
        return 0;
    }
}
