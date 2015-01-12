package de.hsrm.swt02.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents an Form.
 *
 */
public class Form extends RootElement {

    @JsonIgnore
    private static final long serialVersionUID = 2664726035666948658L;
    
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
}