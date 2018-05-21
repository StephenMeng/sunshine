package team.stephen.sunshine.model.common;

import java.io.InputStream;

public class Email {

    /**
     * fromAddress 邮件发送者的地址
     */
    private String fromAddress;

    /**
     * toAddress 邮件接收者的地址
     */
    private String toAddress;

    /**
     * ccAddress 抄送地址
     */
    private String[] ccAddress;

    /**
     * scAddress 密送地址
     */
    private String scAddress;


    /**
     * subject 邮件主题
     */
    private String subject;

    /**
     * content 邮件的文本内容
     */
    private String content;

    /**
     * attachFileNames 邮件附件的文件名
     */
    private String[] attachFileNames;

    /**
     * 附件的inputStream
     */
    private InputStream[] attachFileInputStream;

    private Boolean isHtml;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String[] getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String[] ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getScAddress() {
        return scAddress;
    }

    public void setScAddress(String scAddress) {
        this.scAddress = scAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] attachFileNames) {
        this.attachFileNames = attachFileNames;
    }

    public InputStream[] getAttachFileInputStream() {
        return attachFileInputStream;
    }

    public void setAttachFileInputStream(InputStream[] attachFileInputStream) {
        this.attachFileInputStream = attachFileInputStream;
    }

    public Boolean getHtml() {
        return isHtml;
    }

    public void setHtml(Boolean html) {
        isHtml = html;
    }
}
