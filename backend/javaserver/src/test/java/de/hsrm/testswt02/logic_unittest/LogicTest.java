package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This Testclass tests the logic interface.
 *
 */
public class LogicTest {

    Logic li;
    Workflow w;
    Workflow w1;
    Workflow w2;
    Workflow w3;
    User user;
    User user1;
    User user2;
    Role role;
    Role role1;
    Role role2;
    StartStep startStep;
    StartStep startStep1;
    StartStep startStep2;
    StartStep startStep3;
    Action action;
    Action action1;
    Action action2;
    Action action3;
    FinalStep finalStep;

    /**
     * 
     * @throws LogicException 
     */
    @Test
    public void addGetWorkflowTest() throws LogicException {
        init();
        li.addWorkflow(w);
        assertTrue(li.getWorkflow(w.getId()).equals(w));
    }

    /**
     * 
     * @throws LogicException 
     */
    @Test
    public void startWorkflowTest() throws LogicException {
        init();
        initExtension();

        li.addWorkflow(w);

        li.startWorkflow(w.getId(), user.getUsername()); // added
                                                         // user.getUsername
                                                         // because
                                                         // startWorkflow
                                                         // expects String not
                                                         // user

        final Workflow workflow = li.getWorkflow(w.getId());
        assertFalse(workflow.getItems().isEmpty());
    }

    /**
     * 
     * @throws LogicException 
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void deleteWortflowTest() throws LogicException {
        init();
        li.addWorkflow(w);
        li.deleteWorkflow(w.getId());
        
        li.getWorkflow(w.getId());

    }

    /**
     * 
     * @throws LogicException 
     */
    @Test
    public void stepOverTest() throws LogicException {
        init();
        initExtension();
        li.addWorkflow(w);
        li.startWorkflow(w.getId(), user.getUsername());
        final Workflow workflow = li.getWorkflow(w.getId());
        Item item = workflow.getItemByPos(0);
        
        li.stepForward(item.getId(), workflow.getStepById(action.getId())
                .getId(), user.getUsername());
        
        li.stepForward(item.getId(), workflow.getStepById(action.getId())
                .getId(), user.getUsername());

        item = li.getItem(item.getId());
        assertTrue(item.getStepState(action.getId()) == "DONE");
    }

    /**
     * 
     * @throws LogicException 
     */
    @Test
    public void addStepTest() throws LogicException {
        init();
        li.addWorkflow(w);
        final int i = w.getSteps().size();
        li.addStep(w.getId(), new Action());
        final Workflow workflow = li.getWorkflow(w.getId());
        assertTrue(workflow.getSteps().size() == i + 1);

    }

  
    /**
     * 
     * @throws PersistenceException 
     */
    @Test
    public void addGetUserTest() throws PersistenceException {
        init();
        try {
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
        
        assertTrue(li.getUser(user.getUsername()).equals(user));
    }

    /**
     * 
     * @throws PersistenceException 
     */
    @Test(expected = UserNotExistentException.class)
    public void deleteUserTest() throws PersistenceException {
        init();
        try {
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
        li.deleteUser(user.getUsername());
        assertTrue(li.getUser(user.getUsername()) == null);
    }

    /**
     * 
     * @throws LogicException if something went wrong in logic
     * @throws UserNotExistentException 
     * @throws WorkflowNotExistentException 
     * @throws StorageFailedException 
     */
    @Test
    public void getWorkflowsByUser() throws LogicException, WorkflowNotExistentException, UserNotExistentException, StorageFailedException {
        init();
        initExtension();

        final int i = li.getAllWorkflowsForUser(user.getUsername()).size();
        assertTrue(i == 1);

    }

    /**
     * 
     * @throws LogicException 
     */
    @Test
    public void getOpenItemsByUserTest() throws LogicException {
        init();
        initExtension();
        
        li.startWorkflow(w.getId(), user.getUsername());
        li.startWorkflow(w.getId(), user.getUsername());

        final int i = li.getRelevantItemsForUser(w.getId(), user.getUsername()).size();
        assertTrue(i == 2);

    }
    
    /**
     * Check if steps in a workflow are connected.
     * @throws LogicException 
     */
    @Test
    public void connectWorkflowSteps() throws LogicException {
        init();
        initExtension();
        
        for (Step step: w2.getSteps()) {
            if (!(step instanceof FinalStep)) {
                assertTrue(step.getNextSteps().size() == 1);
            }
        }
    }

    /**
     * 
     * @throws LogicException if something went wrong in logic.
     * @throws WorkflowNotExistentException 
     * @throws UserNotExistentException 
     * @throws StorageFailedException 
     */
    @Test
    public void getStartableWorkflowsTest() throws LogicException, UserNotExistentException, WorkflowNotExistentException, StorageFailedException {
        init();
        initExtension();
        final int i = li.getStartableWorkflowsForUser(user2.getUsername()).size();

        assertTrue(i == 2);
    }

    /**
     * This Method test deactivation.
     * @throws LogicException 
     */
    @Test
    public void deactivateWorkflow() throws LogicException {
        init();
        li.addWorkflow(w);
        w.setActive(false);
        assertFalse(w.isActive());
    }
    
    
    /**
     * 
     * @throws UserNotExistentException if user doesnt exisis.
     * @throws LogicException if something went wrong in logic.
     * @throws WorkflowNotExistentException 
     * @throws StorageFailedException 
     */
    @Test
    public void getAllActiveWorkflowsByUserTest() throws UserNotExistentException, LogicException, WorkflowNotExistentException, StorageFailedException {
        init();
        initExtension();       
        final int before = li.getAllWorkflowsForUser(user.getUsername()).size();
        
        li.deactivateWorkflow(w.getId());
        
         
        final int after = li.getAllWorkflowsForUser(user.getUsername()).size();

        assertTrue(before == after + 1);
        
    }
    /**
     * open items should show even the workflow is deaktive.
     * @throws LogicException .
     */
 
    

    /**
     * this method init basic data for testing.
     */
    private void init() {
        final Injector i = Guice.createInjector(new SingleModule());
        li = i.getInstance(Logic.class);
        
        role = new Role();
        role.setRolename("role");
        
        user = new User();
        user.setUsername("0");
        user.getRoles().add(role);
        
        final ArrayList<String> roles = new ArrayList<String>();
        roles.add(role.getRolename());
        
        startStep = new StartStep(roles);
        action = new Action(roles, "description");
        finalStep = new FinalStep();

        w = new Workflow();
        w.addStep(startStep);
        w.addStep(action);
        w.addStep(finalStep);
        
        try {
            li.addRole(role);
            li.addUser(user);
            li.addWorkflow(w);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (LogicException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method init more data for testing.
     * @throws LogicException 

     */
    private void initExtension() throws LogicException {
        
        role1 = new Role();
        role1.setRolename("role1");
        
        role2 = new Role();
        role2.setRolename("role2");
        
        user1 = new User();
        user1.setUsername("1");
        user1.getRoles().add(role1);
        
        user2 = new User();
        user2.setUsername("2");
        user2.getRoles().add(role2);
        
        final ArrayList<String> roles1 = new ArrayList<String>();
        roles1.add(role1.getRolename());
        final ArrayList<String> roles2 = new ArrayList<String>();
        roles2.add(role2.getRolename());
        
        startStep1 = new StartStep(roles1);
        startStep2 = new StartStep(roles2);
        startStep3 = new StartStep(roles2);
        action1 = new Action(roles1, "description");
        action2 = new Action(roles2, "description");
        action3 = new Action(roles2, "description");
        finalStep = new FinalStep();
        
        w1 = new Workflow();
        w1.addStep(startStep1);
        w1.addStep(action1);
        w1.addStep(finalStep);
        
        w2 = new Workflow();
        w2.addStep(startStep2);
        w2.addStep(action2);
        w2.addStep(new FinalStep());
        
        w3 = new Workflow();
        w3.addStep(startStep3);
        w3.addStep(action3);
        w3.addStep(new FinalStep());
        
        try {
            li.addWorkflow(w1);
            li.addWorkflow(w2);
            li.addWorkflow(w3);
        
        } catch (IncompleteEleException e1) {
            e1.printStackTrace();
        }
        
        try {
            li.addRole(role1);
            li.addRole(role2);
            li.addUser(user2);
            li.addUser(user1);
            
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
    }

}
