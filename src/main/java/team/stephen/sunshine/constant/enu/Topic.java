package team.stephen.sunshine.constant.enu;

import team.stephen.sunshine.model.common.HistoryLog;

public enum Topic {
    EMAIL("email", null),
    LOG("log", HistoryLog.class);
    private String name;
    private Class cls;

    Topic(String name, Class cls) {
        setName(name);
        setCls(cls);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public static Topic getTopic(String topic) {
        for (Topic t : Topic.values()) {
            if (t.getName().equals(topic)) {
                return t;
            }
        }
        return null;
    }
}
