package de.hsrm.testswt02.logic_unittest;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;

/**
 * This class tests the process of editting a workflow.
 *
 */
public class WorkflowEditingTest {
    
    Logic logic;
    Workflow workflow, workflow2;
    StartStep startStep, startStep2;
    Action action, action2, action3;
    FinalStep finalStep, finalStep2;
    User user;
    Role role;
    
    
    /**
     * Setup before starting Test.
     */
    @Before
    public void startup() {
        final Injector i = Guice.createInjector(new SingleModule());
        logic = i.getInstance(Logic.class);
        
        role = new Role();
        role.setRolename("role");
        
        user = new User();
        user.setUsername("user");
        user.getRoles().add(role);
        
        final ArrayList<String> roles = new ArrayList<String>();
        roles.add(role.getRolename());

        startStep = new StartStep(roles);
        startStep2 = new StartStep(roles);
        action = new Action(roles, "description");
        action2 = new Action(roles, "description von action 2");
        action3 = new Action(roles, "description von action 3");
        finalStep = new FinalStep(roles);
        finalStep2 = new FinalStep(roles);
        
        workflow = new Workflow();
        workflow.addStep(startStep);
        workflow.addStep(action);
        workflow.addStep(finalStep);
        workflow2 = new Workflow();
        workflow2.addStep(startStep2);
        workflow2.addStep(action2);
        workflow2.addStep(action3);
        workflow2.addStep(finalStep2);
        
        try {
            logic.addRole(role);
            logic.addUser(user);
            logic.addWorkflow(workflow);
        } catch (LogicException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Tests editing a workflow which has no items.
     * Workflow should be overwrited with new workflow.
     * @throws PersistenceException if problems occurs while persisting.
     */
    @Test
    public void editWorkflowWithoutItems() throws PersistenceException {
        final int oldSize = 3;
        final int newSize = 4;
        
        workflow2.setId(workflow.getId());
        assertTrue(logic.getWorkflow(workflow.getId()).getSteps().size() == oldSize);
        try {
            logic.addWorkflow(workflow2);
        } catch (LogicException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getWorkflow(workflow.getId()).getSteps().size() == newSize);
    }
    
    /**
     * Tests editing a workflow which has finished items.
     * Workflow should be overwrited with new workflow. Items should be still available.
     * @throws LogicException if problems occurs while persisting.
     */
    @Test
    public void editWorkflowWithFinishedItems() throws LogicException {
        final Item item = new Item();
        final Item item2 = new Item();
        final int oldSize = 3;
        final int newSize = 4;
        
        item.setFinished(true);
        item2.setFinished(true);
        workflow.addItem(item);
        workflow2.addItem(item2);
        logic.addWorkflow(workflow);
        workflow2.setId(workflow.getId());
        assertTrue(logic.getWorkflow(workflow.getId()).getItems().size() == 1);
        assertTrue(logic.getWorkflow(workflow.getId()).getSteps().size() == oldSize);
        try {
            logic.addWorkflow(workflow2);
        } catch (LogicException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getWorkflow(workflow.getId()).getSteps().size() == newSize);
        assertTrue(logic.getWorkflow(workflow.getId()).getItems().size() == 1);
    }
    
    /**
     * Tests editing a active workflow with unfinished items.
     * Workflow should be deactive and a new workflow should be available. 
     * The Item should be still available and unfinished.
     * @throws LogicException if problems occurs while persisting.
     */
    @Test
    public void editWorkflowWithUnfinishedItems() throws LogicException {
        final int oldSize = 2;
        final int newSize = 3;
        
        logic.startWorkflow(workflow.getId(), user.getId());
        workflow2.setId(workflow.getId());
        assertTrue(logic.getAllWorkflows().size() == oldSize);
        try {
            logic.addWorkflow(workflow2);
        } catch (LogicException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getWorkflow(workflow.getId()).isActive() == false);
        assertTrue(logic.getWorkflow(workflow.getId()).getItems().size() == 1);
        assertTrue(logic.getWorkflow(workflow.getId()).getItemByPos(0).isFinished() == false);
        assertTrue(logic.getAllWorkflows().size() == newSize);
    }
    
    /**
     * Tests editing a deactivated workflow with unfinished items.
     * Workflow should still be deactivated and its item should be still unfinished.
     * Also a new workflow should have been added without items. 
     * @throws LogicException if problems occurs while persisting.
     */
    @Test
    public void editDeactivatedWorkflowWithUnfinishedItems() throws LogicException {
        final int oldSize = 2;
        final int newSize = 3;
        
        logic.startWorkflow(workflow.getId(), user.getId());
        workflow.setActive(false);
        workflow2.setId(workflow.getId());
        assertTrue(logic.getAllWorkflows().size() == oldSize);
        try {
            logic.addWorkflow(workflow2);
        } catch (LogicException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getWorkflow(workflow.getId()).isActive() == false);
        assertTrue(logic.getWorkflow(workflow.getId()).getItems().size() == 1);
        assertTrue(logic.getWorkflow(workflow.getId()).getItemByPos(0).isFinished() == false);
        assertTrue(logic.getAllWorkflows().size() == newSize);
        //new workflow added therefore index 2
        assertTrue(logic.getAllWorkflows().get(2).getItems().size() == 0);
    }
    
    /**
     * Tests updating workflow state (from active to deactive).
     * @throws PersistenceException if problems occurs while persisting.
     */
    @Test
    public void workflowStateUpdate() throws PersistenceException {
        workflow.setActive(false);
        try {
            logic.addWorkflow(workflow);
        } catch (LogicException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getWorkflow(workflow.getId()).isActive() == false);
    }
    
}
