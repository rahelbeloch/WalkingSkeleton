package model;

public enum MetaState {

    INACTIVE,
    OPEN,
    BUSY,
    DONE;

    public String value() {
        return name();
    }

    public static MetaState fromValue(String v) {
        return valueOf(v);
    }

}
