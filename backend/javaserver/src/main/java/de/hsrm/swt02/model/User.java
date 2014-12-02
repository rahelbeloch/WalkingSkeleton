package de.hsrm.swt02.model;

/**
 * This class represents an User. A User is a manifestation of a RootElement.
 */
public class User extends RootElement {
    // Used for (de)serialization. Do not change.
    private String username;

    /**
     * Constructor for User.
     */
    public User() {
        super();
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
}