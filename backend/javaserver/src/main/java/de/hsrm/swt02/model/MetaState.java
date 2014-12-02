package de.hsrm.swt02.model;

/**
 * Enum for the state of a Step, meant to be used in the list MetaEntry.
 */
public enum MetaState {

    // values
    INACTIVE, OPEN, BUSY, DONE;

    /**
     * value getter.
     * @return name()
     */
    public String value() {
        return name();
    }

    /**
     * valueof getter.
     * @param v is string corresponding the enum value
     * @return int
     */
    public static MetaState fromValue(String v) {
        return valueOf(v);
    }

}
