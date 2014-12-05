package de.hsrm.swt02.businesslogic;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.processors.StartTrigger;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
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
    private UseLogger logger;

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
        this.logger = logger;
        setLogicResponse(new LogicResponse());
    }

    /**
     * This method starts a Workflow.
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
        final StartTrigger start = new StartTrigger(workflow, pm, p);
        start.startWorkflow();
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow_def" + workflowID));
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
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow_def" + workflow.getId()));
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
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow_del" + workflowID));
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
    
//    @Override
//    public void stepFinished(int itemId, int stepId, String username) throws ItemNotExistentException, UserNotExistentException {
//        pm.selectProcessor(p.loadStep(stepId), p.loadItem(itemId), p.loadUser(username), "finish");
//    }
    

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
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow_upd" + workflow.getId()));
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
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow_upd" + workflow.getId()));
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
        logicResponse.add(new Message("USER_INFO", "user_def" + user.getUsername()));
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
        // System.out.println("Do you really want to delete a user?");
        p.deleteUser(username);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("USER_INFO", "user_del" + username));
        return logicResponse;
    }

    /**
     * This method returns all workflows, in which the user is involved.
     * 
     * @param user
     * @return a LinkedList of workflows
     */
    @Override
    public List<Workflow> getWorkflowsByUser(String username) {
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : p.loadAllWorkflows()) {
            for (Step step : wf.getSteps()) {
                logger.log(Level.INFO, "step:" + step.getId() + "user:" +  step.getUsername());

                if (step.getUsername().equals(username)) {
                    workflows.add((Workflow) wf);
                    break;
                }
            }
        }
        return workflows;
    }

    /**
     * This method returns all actual Items for a User.
     * 
     * @param user
     * @return a LinkedList, with actual Items
     */
    @Override
    public List<Item> getOpenItemsByUser(String username) {

        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getWorkflowsByUser(username);
        final LinkedList<Item> items = new LinkedList<Item>();

        for (Workflow wf : workflows) {
            for (Item item : wf.getItems()) {

                if ((wf.getStepById(Integer
                        .parseInt(item.getActStep().getKey())).getUsername())
                        .equals(username)) 
                {
                    items.add(item);
                    break;
                }
            }
        }
        return items;
    }

    /**
     * This method returns all Workflows, which can be startes by this user.
     * 
     * @param user
     * @return
     */
    @Override
    public List<Workflow> getStartableWorkflows(String username) {

        final LinkedList<Workflow> startableWorkflows = new LinkedList<Workflow>();
        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getWorkflowsByUser(username);

        for (Workflow wf : workflows) {
            if (wf.getStepByPos(0).getUsername().equals(username)) {
                startableWorkflows.add(wf);
            }
        }
        return startableWorkflows;
    }

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
    public List<Workflow> getAllWorkflow() {
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

   
}
