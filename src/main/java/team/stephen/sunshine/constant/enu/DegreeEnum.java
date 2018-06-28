package team.stephen.sunshine.constant.enu;

/**
 * Response 返回参数
 *
 * @author stephen
 * @date 2018/5/21
 */

public enum DegreeEnum {
    POST_DOC(1, "博士后", "post doctor"),
    doctor(2, "博士", "doctor"),
    master(3, "硕士", "master"),
    BACHELOR(4, "学士", "bachelor");

    private Integer code;
    private String cn;
    private String en;


    DegreeEnum(int code, String cn, String en) {
        this.code = code;
        this.cn = cn;
        this.en = en;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public static Integer getCodeByCn(String cn) {
        if (cn == null) {
            return null;
        }
        for (DegreeEnum degreeEnum : DegreeEnum.values()) {
            if (degreeEnum.cn.equals(cn)) {
                return degreeEnum.getCode();
            }
        }
        return null;
    }

    public static String getCnByCode(Integer degree) {
        if (degree == null) {
            return null;
        }
        for (DegreeEnum degreeEnum : DegreeEnum.values()) {
            if (degreeEnum.code.equals(degree)) {
                return degreeEnum.getCn();
            }
        }
        return null;
    }
}
