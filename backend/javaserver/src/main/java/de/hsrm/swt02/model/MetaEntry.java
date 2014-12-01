package de.hsrm.swt02.model;

/**
 * This class represents an Entry for the MetaEntryList for an item
 * It's made of a key string and group string to better identify
 * the value string represents the content
 */
public class MetaEntry {
    // Used for (de)serialization. Do not change.
    private String key;

    // Used for (de)serialization. Do not change.
    private String value;

    // Used for (de)serialization. Do not change.
    private String group;

    /**
     * Constructor for MetaEntry
     */
    public MetaEntry() {

    }

    /**
     * Key getter
     */
    public String getKey() {
        return key;
    }

    /**
     * Key setter
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Value getter
     */
    public String getValue() {
        return value;
    }

    /**
     * Value setter
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Group getter
     */
    public String getGroup() {
        return group;
    }

    /**
     * Group setter
     */
    public void setGroup(String group) {
        this.group = group;
    }
}
