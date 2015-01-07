package de.hsrm.swt02.model;

import java.util.HashMap;
/**
 * This class represents an Form.
 *
 */
public class Form extends RootElement {

    HashMap<String,String> formDef; 
    String description;
    
    /**
     * 
     */
    public Form() {
        super();
        formDef = new HashMap<String, String>();
        description = "";
    }
    /**
     * 
     * @param description the Desciption of a Form
     */
    public Form(String description) {
        super();
        this.description = description;
        formDef = new HashMap<String, String>();
    }
    /**
     * 
     * @return the formDef
     */
    public HashMap<String, String> getFormDef() {
        return formDef;
    }
    /**
     * 
     * @return the Description
     */
    public String getDescription() {
        return description;
    }
    /**
     * 
     * @param description sets a new Description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
