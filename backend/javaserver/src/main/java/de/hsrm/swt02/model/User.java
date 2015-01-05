package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an User. A User is a manifestation of a RootElement.
 */
public class User extends RootElement {
    // Used for (de)serialization. Do not change.
    private List<Role> roles;
    // Used for (de)serialization. Do not change.
    private List<String> messagingSubs;
    // a users private password
    private String password;

    /**
     * Constructor for User.
     */
    public User() {
        super();
        roles = new ArrayList<Role>();
    }

    /**
     * Username getter.
     * 
     * @return username is the username
     */
    public String getUsername() {
        return id;
    }

    /**
     * Username setter.
     * 
     * @param username
     *            is the username
     */
    public void setUsername(String username) {
        this.id = username;
    }
    
    /**
     * Password getter.
     * 
     * @return password - a users password
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Password setter.
     * 
     * @param password - a users password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Roles getter. There is no setter because roles is an ArrayList.
     * 
     * @return roles is the list of roles of the user
     */
    public List<Role> getRoles() {
        return this.roles;
    }
    
    /**
     * Subs getter. There is no setter because roles is an ArrayList.
     * 
     * @return roles is the list of roles of the user
     */
    public List<String> getMessagingSubs() {
        return this.messagingSubs;
    }

    /**
     * Init method.
     * 
     * @param u
     *            is the user we want to init
     */
    public void init(User u) {
        super.init(u);
        setUsername(u.getUsername());
    }

    /**
     * Deep Copy - Cloning method for Actions.
     * 
     * @exception CloneNotSupportedException
     *                clone convention
     * @throws CloneNotSupportedException
     * @return clone is the clone of the action
     */
    public Object clone() throws CloneNotSupportedException {
        final User clone = new User();
        clone.init(this);

        for (Role role : this.roles) {
            final Role cloneRole = (Role) role.clone();
            clone.getRoles().add(cloneRole);
        }
        
        clone.setPassword(this.password);

        return clone;
    }
}