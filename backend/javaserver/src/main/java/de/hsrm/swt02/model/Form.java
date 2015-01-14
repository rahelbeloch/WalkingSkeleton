package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents an Form.
 *
 */
public class Form extends RootElement {

    @JsonIgnore
    private static final long serialVersionUID = 2664726035666948658L;
//    private HashMap<String,String> formDef; 
    private List<FormEntry> formDef;
    private String description;
    
    /**
     * Default Constructor.
     */
    public Form() {
        super();
        this.id = "";
//        formDef = new HashMap<String, String>();
        formDef = new ArrayList<FormEntry>();
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
//        formDef = new HashMap<String, String>();
        formDef = new ArrayList<FormEntry>();
    }
    
//    /**
//     * 
//     * @return the formDef
//     */
//    public HashMap<String, String> getFormDef() {
//        return formDef;
//    }
    
    /**
     * Getter for formDef.
     * @return formDef list
     */
    public List<FormEntry> getFormDef() {
        return this.formDef;
    }
    
//    /**
//     * 
//     * @param formDef Map
//     */
//    public void setFormDef(HashMap<String,String> formDef) {
//        this.formDef = formDef;
//    }
    
    /**
     * Setter for formDef.
     * @param formDef indicates value which is setted
     */
    public void setFormDef(List<FormEntry> formDef) {
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
    
    /**
     * Init method.
     * @param f is the form we want to init
     */
    public void init(Form f) {
        super.init(f);
        setDescription(f.getDescription());
        setFormDef(f.getFormDef());
    }
    
    /**
     * Deep Copy - Cloning method for Actions.
     * @exception CloneNotSupportedException clone convention
     * @throws CloneNotSupportedException
     * @return clone is the clone of the action
     */
    public Object clone() throws CloneNotSupportedException {
        final Form clone = new Form();
        clone.init(this);
        return clone;
    }
    
    @Override
    public String toString() {
        String ret = "";
        ret += "Form: ";
        ret += this.id;
        ret += " - " + this.description;
        
        return ret;
    }
}