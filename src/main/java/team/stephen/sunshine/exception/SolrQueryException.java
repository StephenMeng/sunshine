package team.stephen.sunshine.exception;

/**
 * Created by stephen on 2018/3/16.
 */

public class SolrQueryException extends Exception {
    private String msg;

    public SolrQueryException(String msg) {
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
