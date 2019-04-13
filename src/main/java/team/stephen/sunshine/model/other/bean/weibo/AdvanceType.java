package team.stephen.sunshine.model.other.bean.weibo;

import com.google.common.base.Objects;
import team.stephen.sunshine.exception.CrawlException;

/**
 * @author Stephen
 * @date 2019/04/06 10:46
 */
public enum AdvanceType {
    ALL("all", "typeall", "1"),

    HOT("hot", "xsort", "hot");

    private String type;
    private String key;
    private String value;

    AdvanceType(String t, String k, String v) {
        this.type = t;
        this.key = k;
        this.value = v;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static AdvanceType getFromString(String t) throws CrawlException {
        for (AdvanceType type : AdvanceType.values()) {
            if (Objects.equal(type.getType(), t)) {
                return type;
            }
        }
        throw new CrawlException("Empty enum type for :{}", t);
    }
}
