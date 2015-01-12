package de.hsrm.swt02.model;

import java.util.HashMap;

/**
 * This class represents an Form.
 *
 */
public class Form extends RootElement {

    private HashMap<String,String> formDef; 
    private String description;
    
    /**
     * Default Constructor.
     */
    public Form() {
        super();
        this.id = "";
        formDef = new HashMap<String, String>();
        description = "";
    }
    
    /**
     * 
     * @param description the Desciption of a Form
     */
    public Form(String description) {
        super();
        this.id = "";
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
     * @param formDef Map
     */
    public void setFormDef(HashMap<String,String> formDef) {
        this.formDef = formDef;
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