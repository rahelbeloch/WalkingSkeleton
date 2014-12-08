package de.hsrm.swt02.model;

/**
 * This Class represents a role.
 */
public class Role extends RootElement {
    // Used for (de)serializsation. Do not change!
    private String rolename;
    
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
        return rolename;
    }

    /**
     * Rolename setter.
     * @param rolename short description of role
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
