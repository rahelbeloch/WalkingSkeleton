package de.hsrm.swt02.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DataStorage implements Serializable {

    private static final long serialVersionUID = -891418026625801248L;

    /**
     * abstraction of a database, that persists the data objects workflow, item.
     * user, step, metaEntry
     */
    private List<Workflow> workflows;

    private List<User> users;;

    private List<Role> roles;
    
    private List<Form> forms;
    
    
    public DataStorage() {
        workflows = new LinkedList<Workflow>();
        users = new LinkedList<User>();
        roles = new LinkedList<Role>();
        forms = new LinkedList<Form>();
    }
    
    public DataStorage(List<Workflow> workflows, List<User> users, List<Role> roles, List<Form> forms) {
        this.workflows = workflows != null? workflows : new LinkedList<Workflow>();
        this.users = users != null? users : new LinkedList<User>();
        this.roles = roles != null? roles : new LinkedList<Role>();
        this.forms = forms != null? forms: new LinkedList<Form>();
    }
    
    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    
}
