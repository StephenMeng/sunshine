package team.stephen.sunshine.constant;

public enum ResultEnum {
    PARAMETER_WRONG(400, "参数错误", "Bad Parameter");
    private Integer code;
    private String msgCn;
    private String msgEn;


    ResultEnum(int code, String msgCn, String msgEn) {
        this.code = code;
        this.msgCn = msgCn;
        this.msgEn = msgEn;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsgCn() {
        return msgCn;
    }

    public String getMsgEn() {
        return msgEn;
    }
}
