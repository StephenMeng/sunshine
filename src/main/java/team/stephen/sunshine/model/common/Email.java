package team.stephen.sunshine.model.common;

import org.springframework.mail.SimpleMailMessage;

public class Email {

    /**
     * @FieldName fromAddress 邮件发送者的地址
     */
    private String fromAddress;

    /**
     * @FieldName toAddress 邮件接收者的地址
     */
    private String toAddress;

    /**
     * @FieldName ccAddress 抄送地址
     */
    private String ccAddress;

    /**
     * @FieldName scAddress 密送地址
     */
    private String scAddress;


    /**
     * @FieldName subject 邮件主题
     */
    private String subject;

    /**
     * @FieldName content 邮件的文本内容
     */
    private String content;

    /**
     * @FieldName attachFileNames 邮件附件的文件名
     */
    private String[] attachFileNames;
}
