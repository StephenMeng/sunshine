package team.stephen.sunshine.exception;

/**
 * @author stephen
 * @date 2018/3/16
 */

public class NullParamException extends Exception {
    private String msg;

    public NullParamException(String msg) {
        super(msg);
        setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
