package team.stephen.sunshine.util;


import team.stephen.sunshine.constant.ResultEnum;

/**
 * Created by stephen on 2017/7/15.
 */
public class Response {
    private Integer code;
    private String msg;
    private Object data;

    public static Response success(Object object) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("");
        response.setData(object);
        return response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static Response error(ResultEnum failParamWrong) {
        return Response.error(failParamWrong.getCode(), failParamWrong.getMsgEn(), failParamWrong.getMsgCn());
    }

    public static Response error(Integer code, String msgEn, String msgCn) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msgCn);
        return response;
    }
}
