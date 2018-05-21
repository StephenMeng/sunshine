package team.stephen.sunshine.constant.enu;

/**
 * Response 返回参数
 *
 * @author stephen
 * @date 2018/5/21
 */

public enum ResultEnum {
    /**
     * 参数错误
     */
    PARAMETER_WRONG(400, "参数错误", "Bad Parameter"),
    /**
     * 缺少访问权限
     */
    AUTHORIZE_WRONG(500, "无访问权限", "Request Denied");

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
