package team.stephen.sunshine.util.common;

import team.stephen.sunshine.constant.enu.ResultEnum;

/**
 * @author stephen
 * @date 2018/5/22
 */
public class ParamCheck {
    private static final int ERROR = ResultEnum.CLIENT_ERROR.getCode();
    private Integer code;
    private String msgCn;
    private String msgEn;

    public ParamCheck(Integer code, String mc, String me) {
        setCode(code);
        setMsgCn(mc);
        setMsgEn(me);
    }

    public static ParamCheck error(String mc, String me) {
        return new ParamCheck(ResultEnum.CLIENT_ERROR.getCode(), mc, me);
    }

    public static ParamCheck right() {
        return new ParamCheck(ResultEnum.OK.getCode(), "OK", "OK");
    }

    public boolean error() {
        return ERROR == this.code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsgCn() {
        return msgCn;
    }

    public void setMsgCn(String msgCn) {
        this.msgCn = msgCn;
    }

    public String getMsgEn() {
        return msgEn;
    }

    public void setMsgEn(String msgEn) {
        this.msgEn = msgEn;
    }
}
