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
    
    /**
     * Init method.
     * @param r is the role we want to init
     */
    public void init(Role r) {
        super.init(r);
        this.rolename = r.getRolename();
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
}
