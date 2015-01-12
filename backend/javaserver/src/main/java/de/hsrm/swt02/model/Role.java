package de.hsrm.swt02.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This Class represents a role.
 */
public class Role extends RootElement {
   
    @JsonIgnore
    private static final long serialVersionUID = 7066827168930875656L;

    /**
     * Constructor for Role.
     */
    public Role() {
        super();
    }
    
    /**
     * Rolename getter.
     * @return rolename
     */
    public String getRolename() {
        return id;
    }

    /**
     * Rolename setter.
     * @param rolename short description of role
     */
    public void setRolename(String rolename) {
        this.id = rolename;
    }
    
    /**
     * Init method.
     * @param r is the role we want to init
     */
    public void init(Role r) {
        super.init(r);
        setRolename(r.getRolename());
    }
    
    /**
     * Deep Copy - Cloning method for Actions.
     * @exception CloneNotSupportedException clone convention
     * @throws CloneNotSupportedException
     * @return clone is the clone of the action
     */
    public Object clone() throws CloneNotSupportedException {
        final Role clone = new Role();
        clone.init(this);
        return clone;
    }
    
    @Override
    public String toString() {
        return id;
    }
}
