package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an User. A User is a manifestation of a RootElement.
 */
public class User extends RootElement {
    // Used for (de)serialization. Do not change.
    private String username;
    // Used for (de)serialization. Do not change.
    private List<Role> roles;

    /**
     * Constructor for User.
     */
    public User() {
        super();
        roles = new ArrayList<Role>();
    }

    /**
     * Username getter.
     * @return username is the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Username setter.
     * @param username is the username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Roles getter. There is no setter because roles is an ArrayList.
     * @return roles is the list of roles of the user
     */
    public List<Role> getRoles() {
        return this.roles;
    }
    
    /**
     * Init method.
     * @param r is the role we want to init
     */
    public void init(User u) {
        super.init(u);
        this.username = u.getUsername();
    }
    
    /**
     * Deep Copy - Cloning method for Actions.
     * @exception CloneNotSupportedException clone convention
     * @throws CloneNotSupportedException
     * @return clone is the clone of the action
     */
    public Object clone() throws CloneNotSupportedException {
        final User clone = new User();
        clone.init(this);
        
        for(Role role: this.roles) {
			Role cloneRole = (Role)role.clone();
			clone.getRoles().add(cloneRole);
		}
        
        return clone;
    }
}