package team.stephen.sunshine.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import team.stephen.sunshine.conf.GloableConfig;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.common.Email;
import team.stephen.sunshine.service.common.MailService;
import team.stephen.sunshine.util.common.LogRecord;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author Stephen
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine engine = new TemplateEngine();

    @Override
    public String template(String templateName, Map<String, Object> content) {
        Context context = new Context();
        if (content != null) {
            for (Map.Entry<String, Object> m : content.entrySet()) {
                context.setVariable(m.getKey(), m.getValue());
            }
        }
        return engine.process(templateName, context);
    }

    @Override
    public int sendMail(Email email) throws NullParamException, MessagingException {

        if (!GloableConfig.sendEmail) {
            return 0;
        }
        if (email == null) {
            throw new NullParamException("email can not be null!");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getFromAddress());
            helper.setTo(email.getToAddress());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), email.getHtml());
            helper.setCc(email.getCcAddress());
            if (email.getAttachFileNames() != null) {
                if (email.getAttachFileInputStream() == null) {
                    LogRecord.error("邮件附件列表为空");
                } else if (email.getAttachFileInputStream().length != email.getAttachFileNames().length) {
                    LogRecord.error("邮件附件的名字和附件内容长度不符");
                } else {
                    for (int i = 0; i < email.getAttachFileNames().length; i++) {
                        helper.addAttachment(email.getAttachFileNames()[i], new InputStreamResource(email.getAttachFileInputStream()[i]));
                    }
                }
            }
            mailSender.send(message);
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }
}
