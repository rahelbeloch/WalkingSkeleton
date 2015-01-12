package de.hsrm.swt02.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents an Entry for the MetaEntryList for an item It's made of
 * a key string and group string to better identify the value string represents
 * the content.
 */
public class MetaEntry extends RootElement {
    
    @JsonIgnore
    private static final long serialVersionUID = 7063325675172371786L;

    // Used for (de)serialization. Do not change.
    private String value;

    // Used for (de)serialization. Do not change.
    private String group;
    
 // Used for (de)serialization. Do not change.
    private String opener;

    /**
     * Constructor for MetaEntry.
     */
    public MetaEntry() {

    }

    /**
     * Key getter.
     * 
     * @return key is the key string
     */
    public String getKey() {
        return id;
    }

    /**
     * Key setter.
     * 
     * @param key
     *            is the key string
     */
    public void setKey(String key) {
        this.id = key;
    }

    /**
     * Value getter.
     * 
     * @return value is the value string
     */
    public String getValue() {
        return value;
    }

    /**
     * Value setter.
     * 
     * @param value
     *            is the value string
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Group getter.
     * 
     * @return group is the group string
     */
    public String getGroup() {
        return group;
    }

    /**
     * Group setter.
     * 
     * @param group
     *            is the group string
     */
    public void setGroup(String group) {
        this.group = group;
    }
    
    /**
     * Opener getter.
     * @return opener
     */
    public String getOpener() {
        return opener;
    }

    
    /**
     * Opener setter.
     * @param opener string which indicates opener
     */
    public void setOpener(String opener) {
        this.opener = opener;
    }

    /**
     * Deep Copy - Cloning method for MetaEntries.
     * @exception CloneNotSupportedException convention
     * @throws CloneNotSupportedException
     * @return clone is the requested clone
     */
    public Object clone() throws CloneNotSupportedException {
        final MetaEntry clone = new MetaEntry();
        clone.setGroup(group);
        clone.setKey(id);
        clone.setValue(value);
        clone.setOpener(opener);
        return clone;
    }

    @Override
    public String toString() {
        String ret = "";

        ret += "\t\tKey: " + this.id + "\n";
        ret += "\t\tValue: " + this.value + "\n";
        ret += "\t\tGroup: " + this.group + "\n";
        ret += "\t\tOpener: " + this.opener + "\n";

        return ret;
    }

    

}
