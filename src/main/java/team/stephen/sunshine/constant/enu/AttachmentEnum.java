package team.stephen.sunshine.constant.enu;

/**
 * @author stephen
 * @date 2018/5/22
 */
public enum AttachmentEnum {

    ARTICLE(1),
    USER(2);

    private int type;

    AttachmentEnum(int t) {
        setType(t);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
