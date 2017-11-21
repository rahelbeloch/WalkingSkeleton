package de.hsrm.swt02.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to contain lists of Users, Workflows, Roles and Forms. Simplifies the Serialization,
 * by saving data in one object of DataStorage and de-/serializing the complete object.
 * @author rhaba001
 */
public class DataStorage implements Serializable {

    private static final long serialVersionUID = -891418026625801248L;

    /**
     * abstraction of a database, that persists the data objects workflow, item.
     * user, step, metaEntry
     */
    private List<Workflow> workflows;

    private List<User> users;

    private List<Role> roles;
    
    private List<Form> forms;
    
    /**
     * Default constructor. Instantiates the appropriate (empty) lists.
     */
    public DataStorage() {
        workflows = new LinkedList<Workflow>();
        users = new LinkedList<User>();
        roles = new LinkedList<Role>();
        forms = new LinkedList<Form>();
    }
    
    /**
     * Constructor expects four lists of RootElements and saves them. 
     * @param workflows list of workflows for permanent persistence
     * @param users list of users for permanent persistence
     * @param roles list of roles for permanent persistence
     * @param forms list of forms for permanent persistence
     */
    public DataStorage(List<Workflow> workflows, List<User> users, List<Role> roles, List<Form> forms) {
        this.workflows = workflows != null ? workflows : new LinkedList<Workflow>();
        this.users = users != null ? users : new LinkedList<User>();
        this.roles = roles != null ? roles : new LinkedList<Role>();
        this.forms = forms != null ? forms : new LinkedList<Form>();
    }
    
    /**
     * Get-method for workflow list.
     * @return list of workflows
     */
    public List<Workflow> getWorkflows() {
        return workflows;
    }

    /**
     * Set-method for workflows.
     * @param workflows list of workflows to set
     */
    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
    }

    /**
     * Get-method for user list.
     * @return list of user
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Set-method for users.
     * @param users list of users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Get-method for role list.
     * @return list of roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Set-method for roles.
     * @param roles list of roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Get-method for form list.
     * @return list of forms
     */
    public List<Form> getForms() {
        return forms;
    }

    /**
     * Set-method for forms.
     * @param forms list of forms to set
     */
    public void setForms(List<Form> forms) {
        this.forms = forms;
    }
}