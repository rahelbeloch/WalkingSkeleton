package de.hsrm.swt02.model;

/**
 * This class represents an Entry for the MetaEntryList for an item
 * It's made of a key string and group string to better identify
 * the value string represents the content.
 */
public class MetaEntry {
    // Used for (de)serialization. Do not change.
    private String key;

    // Used for (de)serialization. Do not change.
    private String value;

    // Used for (de)serialization. Do not change.
    private String group;

    /**
     * Constructor for MetaEntry.
     */
    public MetaEntry() {

    }

    /**
     * Key getter.
     * @return key is the key string
     */
    public String getKey() {
        return key;
    }

    /**
     * Key setter.
     * @param key is the key string
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Value getter.
     * @return value is the value string
     */
    public String getValue() {
        return value;
    }

    /**
     * Value setter.
     * @param value is the value string
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Group getter.
     * @return group is the group string
     */
    public String getGroup() {
        return group;
    }

    /**
     * Group setter.
     * @param group is the group string
     */
    public void setGroup(String group) {
        this.group = group;
    }
}
