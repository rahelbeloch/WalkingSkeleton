package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents a role.
 */
public class Role extends RootElement {
    // Used for (de)serializsation. Do not change!
    private String rolename;
    //Used for (de)serializsation. Do not change!
    private List<User> users;
    
    /**
     * Constructor for Role.
     */
    public Role() {
        super();
        users = new ArrayList<User>();
    }
    
    /**
     * Constructor for Role with parameters.
     * @param id is the id of the role
     */
    public Role(int id) {
        super();
        this.id = id;
        users = new ArrayList<User>();
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
     * Users getter. There is no setter because users is an ArrayList.
     * @return users are the users corresponding to the roles
     */
    public List<User> getUsers() {
        return this.users;
    }

}
