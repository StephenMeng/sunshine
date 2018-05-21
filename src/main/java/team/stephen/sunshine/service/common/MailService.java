package team.stephen.sunshine.service.common;

import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.common.Email;

import javax.mail.MessagingException;
import java.util.Map;

public interface MailService {
    /**
     * 根据模板生成html格式的邮件正文
     *
     * @param templateName 模板名，不带html
     * @param content      模板内容map
     * @return html正文
     */
    String template(String templateName, Map<String, Object> content);

    /**
     * 发送邮件
     *
     * @param email 邮件实体类
     * @return 发送结果代码
     * @throws NullParamException 空指针
     * @throws MessagingException 错误信息
     */
    int sendMail(Email email) throws NullParamException, MessagingException;
}
