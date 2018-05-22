package team.stephen.sunshine.constant.enu;

/**
 * Response 返回参数
 *
 * @author stephen
 * @date 2018/5/21
 */

public enum ResultEnum {
    CLIENT_ERROR(400, "客户端错误", "Client Error"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR(401, "参数错误", "Bad Parameter"),

    /**
     * 空参数错误
     */
    NULL_PARAMETER(402, "参数为空！", "Null Parameter"),
    /**
     * 缺少访问权限
     */
    SERVER_WRONG(500, "服务器错误", "Server Wrong"),
    AUTHORIZE_ERROR(501, "无访问权限", "Request Denied");


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
