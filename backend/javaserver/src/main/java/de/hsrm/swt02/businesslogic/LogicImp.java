package de.hsrm.swt02.businesslogic;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.RoleAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserHasAlreadyRoleException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

/**
 * This class implements the logic interface and is used for logic operations.
 */
public class LogicImp implements Logic {

    private Persistence p;
    private ProcessManager pm;
    private LogicResponse logicResponse;

    /**
     * Constructor for LogicImp.
     * 
     * @param p
     *            is a singleton instance of the persistence
     * @param pm
     *            is a singleton instance of the processmanager
     *            
     * @param logger 
     *            is the logger instance for this application
     */
    @Inject
    public LogicImp(Persistence p, ProcessManager pm, UseLogger logger) {
        this.p = p;
        this.pm = pm;
        setLogicResponse(new LogicResponse());
    }

    /**
     * This method starts a Workflow via processmanager.
     * 
     * @param workflowID
     *            the workflow, which should be started
     * @param user
     *            the User, who starts the workflow
     * @throws WorkflowNotExistentException
     */
    @Override
    public LogicResponse startWorkflow(int workflowID, String username) throws WorkflowNotExistentException {
        final Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        pm.startWorkflow(workflow, username);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=def=" + workflowID));
        return logicResponse;
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param workflow
     */
    @Override
    public LogicResponse addWorkflow(Workflow workflow) {
        p.storeWorkflow(workflow);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=def=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method loads a Workflow.
     * 
     * @param workflowID
     *            describe the workflow
     * @return a Workflow, if there is one, who has this workflowID
     * @throws WorkflowNotExistentException
     */
    @Override
    public Workflow getWorkflow(int workflowID) throws WorkflowNotExistentException {
        return (Workflow) p.loadWorkflow(workflowID);
    }

    /**
     * This method delete a Workflow in Persistence.
     * 
     * @param workflowID
     *            describe the Workflow
     * @throws WorkflowNotExistentException
     */
    @Override
    public LogicResponse deleteWorkflow(int workflowID) throws WorkflowNotExistentException {
        p.deleteWorkflow(workflowID);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=del=" + workflowID));
        return logicResponse;
    }

    /**
     * This method execute a step in an item.
     * 
     * @param item
     *            the Item, which edited
     * @param step
     *            the step, which execute
     * @param user
     *            , who execute the step in the Item
     * @throws UserNotExistentException
     * @throws ItemNotExistentException
     */
    @Override
    public void stepForward(int itemId, int stepId, String username) throws ItemNotExistentException, UserNotExistentException {
        pm.executeStep(p.loadStep(stepId), p.loadItem(itemId), p.loadUser(username));
    }

    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID
     *            the workflow, which shall edited
     * @param step
     *            the step, which shall added
     * @throws WorkflowNotExistentException
     */
    @Override
    public LogicResponse addStep(int workflowID, Step step) throws WorkflowNotExistentException {
        final Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        workflow.addStep(step);
        p.storeWorkflow(workflow);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID
     *            the workflow, which shall edited
     * @param stepID
     *            the step, which shall delete
     * @throws WorkflowNotExistentException
     */
    @Override
    public LogicResponse deleteStep(int workflowID, int stepID) throws WorkflowNotExistentException {
        final Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        workflow.removeStep(stepID);
        p.storeWorkflow(workflow);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws UserAlreadyExistsException
     */
    @Override
    public LogicResponse addUser(User user) throws UserAlreadyExistsException {
        p.addUser(user);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("USER_INFO", "user=def=" + user.getUsername()));
        return logicResponse;
    }

    /**
     * This method loads a User.
     * 
     * @param username
     *            describe the user
     * @return a User, if there is one, who has this username
     * @throws UserNotExistentException
     */
    @Override
    public User getUser(String username) throws UserNotExistentException {
        return (User) p.loadUser(username);
    }

    /**
     * This method delete a User.
     * 
     * @param username
     *            describe the user
     * @throws UserNotExistentException
     */
    @Override
    public LogicResponse deleteUser(String username) throws UserNotExistentException {
        p.deleteUser(username);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("USER_INFO", "user=del=" + username));
        return logicResponse;
    }

    /**
     * This method returns all workflows, in which the user is involved.
     * 
     * @param user
     * @return a LinkedList of workflows
     * @throws WorkflowNotExistentException 
     */
    @Override
    public List<Workflow> getAllWorkflowsByUser(String username) throws WorkflowNotExistentException, UserNotExistentException {
        User user = p.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : p.loadAllWorkflows()) {
            for (Step step : wf.getSteps()) {
                if (step.getUsername().equals(username)) {
                	Workflow copyOfWf = wf;
                	copyOfWf.clearItems();
                    workflows.add(copyOfWf);
                    break;
                }
            }
        }
        return workflows;
    }

//    /**
//     * This method returns all actual Items for a User.
//     * 
//     * @param user
//     * @return a LinkedList, with actual Items
//     * @throws WorkflowNotExistentException 
//     */
//    public List<Item> getOpenItemsByUser(String username) throws WorkflowNotExistentException, UserNotExistentException {
//
//        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getWorkflowsByUser(username);
//        final LinkedList<Item> items = new LinkedList<Item>();
//
//        
//        for (Workflow wf : workflows) {
//            for (Item item : wf.getItems()) {
//
//                if ((wf.getStepById(Integer
//                        .parseInt(item.getActStep().getKey())).getUsername())
//                        .equals(username)) 
//                {
//                    items.add(item);
//                }
//            }
//        }
//        return items;
//    }
    
    @Override
    public List<Integer> getStartableWorkflowsByUser(String username) throws UserNotExistentException, WorkflowNotExistentException {
    	List<Integer> startableWorkflows = new LinkedList<>();
    	for(Workflow workflow: getAllWorkflowsByUser(username)) {
    		Step startStep = workflow.getSteps().get(0);
    		assert(startStep instanceof StartStep);
    		if(startStep.getUsername() == username) {
    			startableWorkflows.add(workflow.getId());
    		}
    	}
    	return startableWorkflows;
    }
    
    @Override
    public List<Item> getRelevantItemsByUser(int workflowId, String username) throws UserNotExistentException, WorkflowNotExistentException {
    	LinkedList<Item> relevantItems = new LinkedList<>();
    	for(Workflow usersWorkflow: getAllWorkflowsByUser(username)) {
    		for(Item item: usersWorkflow.getItems()) {
    			MetaEntry me = item.getActStep();
    			if(me != null) {
    				relevantItems.add(item);
    			}
    		}
    	}
    	return relevantItems;
    }

//    /**
//     * This method returns all Workflows, which can be startes by this user.
//     * 
//     * @param user
//     * @return
//     * @throws WorkflowNotExistentException 
//     */
//    public List<Workflow> getStartableWorkflows(String username) throws WorkflowNotExistentException, UserNotExistentException {
//
//        final LinkedList<Workflow> startableWorkflows = new LinkedList<Workflow>();
//        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getWorkflowsByUser(username);
//
//        for (Workflow wf : workflows) {
//            if (wf.getStepByPos(0).getUsername().equals(username)) {
//                startableWorkflows.add(wf);
//            }
//        }
//        return startableWorkflows;
//    }

    /**
     * This method gets a LogicResponse object from the processmanager instance.
     * 
     * @return processmanager's logicResponse object
     */

    public LogicResponse getProcessLogicResponse() {
        return pm.getLogicResponse();
    }
    
    /**
     * Setter for LogicResponse object from the processmanager instance.
     * @param lr new LogicResponse object for processmanager logicResponse
     */
    public void setProcessLogicResponse(LogicResponse lr) {
        pm.setLogicResponse(lr);
        
    }
    
    /**
     * Getter for logicResponse.
     * @return logicResponse
     */
    public LogicResponse getLogicResponse() {
        return logicResponse;
    }

    /**
     * Setter for logicResponse.
     * @param lr is new value for logicResponse
     */
    public void setLogicResponse(LogicResponse lr) {
        this.logicResponse = lr;
    }
    /**
     * This method return all workflows in persistence.
     * @return
     */
    @Override
    public List<Workflow> getAllWorkflows() throws WorkflowNotExistentException {
        return p.loadAllWorkflows();
    }

    /**
     * This method check a User, later it will be extended for password.
     * @param username the user, to be checked
     * @return if user existing true, else false
     */
    @Override
    public boolean checkLogIn(String username) throws UserNotExistentException {
        try {
            p.loadUser(username);
        } catch (UserNotExistentException e) {
            throw e;
        }
        return true;
    }
    
    
    // BusinessLogic Sprint 2
    
    /**
     * This method returns all roles in persistence.
     * @return p.loadAllRoles is the list of all Roles
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    public List<Role> getAllRoles() throws RoleNotExistentException {
        return p.loadAllRoles();
    }
    
    /**
     * This method returns all users in persistance.
     * @return p.loadAll Users is the list of all users
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserNotExistentException
     */
    public List<User> getAllUsers() throws UserNotExistentException {
        return p.loadAllUsers();
    }
    
    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws UserAlreadyExistsException
     */
    @Override
    public LogicResponse addRole(Role role) throws RoleAlreadyExistsException {
        p.storeRole(role);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("ROLE_INFO", "role_def" + role.getRolename()));
        return logicResponse;
    }
    
    /**
     * 
     * @param username
     * @param role
     * @return
     * @throws UserNotExistentException
     * @throws RoleNotExistentException
     * @throws UserHasAlreadyRoleException
     */
    public LogicResponse addRoleToUser (String username, Role role) throws UserNotExistentException, RoleNotExistentException, UserHasAlreadyRoleException {
    	User user = p.loadUser(username);
    	p.addRoleToUser(user, role);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("USER_INFO", "user" + username + "_add_role" + role.getRolename()));
        return logicResponse;
    }
    
    /**
     * 
     * @param rolename
     * @return
     * @throws RoleNotExistentException
     */
    public LogicResponse deleteRole(String rolename) throws RoleNotExistentException {
    	p.deleteRole(rolename);
    	setLogicResponse(new LogicResponse());
    	logicResponse.add(new Message("ROLE-INFO", "role_del" + rolename));
    	return logicResponse;
    }
    
    
}
