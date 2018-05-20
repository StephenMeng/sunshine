package team.stephen.sunshine.constant;

public enum RequestMethodEnum {
    GET("get"),
    POST("post"),
    DELETE("delete");

    RequestMethodEnum(String method) {
        setMethod(method);
    }

    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
