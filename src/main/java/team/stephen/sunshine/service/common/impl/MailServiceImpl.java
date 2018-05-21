package team.stephen.sunshine.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.conf.GloableConfig;
import team.stephen.sunshine.model.common.Email;
import team.stephen.sunshine.service.common.MailService;
import team.stephen.sunshine.util.LogRecod;

import javax.mail.internet.MimeMessage;

/**
 * @author Stephen
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public int sendMail(Email email) {
        if (!GloableConfig.sendEmail) {
            return 0;
        }
        if (email == null) {
            return 0;
        }
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getFromAddress());
            helper.setTo(email.getToAddress());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), email.getHtml());
            helper.setCc(email.getCcAddress());
            if (email.getAttachFileNames() != null) {
                if (email.getAttachFileInputStream() == null) {
                    LogRecod.error("邮件附件列表为空");
                } else if (email.getAttachFileInputStream().length != email.getAttachFileNames().length) {
                    LogRecod.error("邮件附件的名字和附件内容长度不符");
                } else {
                    for (int i = 0; i < email.getAttachFileNames().length; i++) {
                        helper.addAttachment(email.getAttachFileNames()[i], new InputStreamResource(email.getAttachFileInputStream()[i]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
        return 1;
    }
}
