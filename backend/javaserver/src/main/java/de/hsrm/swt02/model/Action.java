package de.hsrm.swt02.model;

/**
 * This class represents an Action. An Action is an manifestation of a Step.
 *
 */
public class Action extends Step {
    // Used for (de)-serialization. Do not change.
    private String description;

    /**
     * Constructor for Action without parameters.
     */
    public Action() {
        super();
    }

    /**
     * Constructor for Action with parameters.
     * @param id id of action
     * @param username name of user responsible for action
     * @param description short description of action
     */
    public Action(int id, String username, String description) {
        this.id = id;
        this.username = username;
        this.description = description;
    }

    /**
     * Description getter.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter.
     * @param description short description of action
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
