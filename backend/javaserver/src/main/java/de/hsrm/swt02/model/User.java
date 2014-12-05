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
}