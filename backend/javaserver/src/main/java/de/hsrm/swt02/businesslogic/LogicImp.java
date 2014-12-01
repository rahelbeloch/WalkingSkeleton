package de.hsrm.swt02.businesslogic;

import java.util.LinkedList;
import java.util.List;

import de.hsrm.swt02.businesslogic.processors.StartTrigger;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.UserNotExistentException;
import de.hsrm.swt02.persistence.WorkflowNotExistentException;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class LogicImp implements Logic {

    private Persistence p;
    private ProcessManager pm;

    @Inject
    public LogicImp(Persistence p, ProcessManager pm) {
        this.p = p;
        this.pm = pm;
    }

    /**
     * This method starts a Workflow
     * 
     * @param workflowID the workflow, which should be started
     * @param user the User, who starts the workflow
     * @throws WorkflowNotExistentException 
     */
    @Override
    public void startWorkflow(int workflowID, User user) throws WorkflowNotExistentException {
        // TODO: check user permission
        Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        StartTrigger start = new StartTrigger(workflow, pm, p);
        start.startWorkflow();
    }

    /**
     * This method store a workflow and distribute a id
     * 
     * @param workflow
     */
    @Override
    public void addWorkflow(Workflow workflow) {
        // TODO: distribute clever ids, may return the id
        p.storeWorkflow(workflow);
    }

    /**
     * This method loads a Workflow
     * 
     * @param workflowID describe the workflow
     * @return a Workflow, if there is one, who has this workflowID
     * @throws WorkflowNotExistentException 
     */
    @Override
    public Workflow getWorkflow(int workflowID) throws WorkflowNotExistentException {
        return (Workflow) p.loadWorkflow(workflowID);
    }

    /**
     * This method delete a Workflow in Persistence
     * 
     * @param workflowID describe the Workflow
     * @throws WorkflowNotExistentException 
     */
    @Override
    public void deleteWorkflow(int workflowID) throws WorkflowNotExistentException {
        p.deleteWorkflow(workflowID);
    }

    /**
     * This method execute a step in an item
     * 
     * @param item the Item, which edited
     * @param step the step, which execute
     * @param user, who execute the step in the Item
     */
    @Override
    public void stepOver(Item item, Step step, User user) {
        pm.selectProcessor(step, item, user);
    }

    /**
     * This method add a step into an existing Workflow
     * 
     * @param workflowID the workflow, which shall edited
     * @param step the step, which shall added
     * @throws WorkflowNotExistentException 
     */
    @Override
    public void addStep(int workflowID, Step step) throws WorkflowNotExistentException {
        Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        workflow.addStep(step);
        p.storeWorkflow(workflow);
    }

    /**
     * This method delete a step from an existing Workflow
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepID the step, which shall delete
     * @throws WorkflowNotExistentException 
     */
    @Override
    public void deleteStep(int workflowID, int stepID) throws WorkflowNotExistentException {
        Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        workflow.removeStep(stepID);
        p.storeWorkflow(workflow);
    }

    /**
     * This method store a workflow and distribute a id
     * 
     * @param user
     * @throws UserAlreadyExistsException
     */
    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        // TODO: distribute clever ids, may return the id
        p.addUser(user);
    }

    /**
     * This method loads a User
     * 
     * @param username describe the user
     * @return a User, if there is one, who has this username
     * @throws UserNotExistentException 
     */
    @Override
    public User getUser(String username) throws UserNotExistentException {
        return (User) p.loadUser(username);
    }

    /**
     * This method delete a User
     * 
     * @param username describe the user
     * @throws UserNotExistentException 
     */
    @Override
    public void deleteUser(String username) throws UserNotExistentException {
        // System.out.println("Do you really really really want to delete a user?");
        p.deleteUser(username);
    }

    /**
     * This method returns all workflows, in which the user is involved
     * 
     * @param user
     * @return a LinkedList of workflows
     */
    @Override
    public List<Workflow> getWorkflowsByUser(User user) {
        LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : p.loadAllWorkflows()) {
            for (Step step : wf.getSteps()) {

                if (step.getUsername().equals(user.getUsername())) {
                    workflows.add((Workflow) wf);
                    break;
                }
            }
        }
        return workflows;
    }

    /**
     * This method returns all actual Items for a User
     * 
     * @param user
     * @return a LinkedList, with actual Items
     */
    @Override
    public List<Item> getOpenItemsByUser(User user) {

        LinkedList<Workflow> workflows = (LinkedList) getWorkflowsByUser(user);
        LinkedList<Item> items = new LinkedList<Item>();

        for (Workflow wf : workflows) {
            for (Item item : wf.getItems()) {

                if ((wf.getStepById(Integer.parseInt(item.getActStep().getKey()))
                        .getUsername()).equals(user.getUsername())) {
                    items.add(item);
                    break;
                }

            }
        }
        return items;
    }

    /**
     * This method returns all Workflows, which can be startes by this user
     * 
     * @param user
     * @return
     */
    @Override
    public List<Workflow> getStartableWorkflows(User user) {

        LinkedList<Workflow> startableWorkflows = new LinkedList<Workflow>();
        LinkedList<Workflow> workflows = (LinkedList) getWorkflowsByUser(user);

        for (Workflow wf : workflows) {
            if (wf.getStepByPos(0).getUsername() == user.getUsername()) {
                startableWorkflows.add(wf);
            }
        }
        return startableWorkflows;
    }
}
