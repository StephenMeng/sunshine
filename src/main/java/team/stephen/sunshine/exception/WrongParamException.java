package team.stephen.sunshine.exception;

/**
 * @author stephen
 * @date 2018/3/16
 */

public class WrongParamException extends Exception {
    private String msg;

    public WrongParamException(String msg) {
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
