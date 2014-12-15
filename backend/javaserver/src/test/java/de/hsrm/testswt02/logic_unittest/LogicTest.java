package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
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
    StartStep startStep;
    StartStep startStep1;
    StartStep startStep2;
    Action action;
    Action action1;
    Action action2;
    FinalStep finalStep;

    /**
     * 
     * @throws WorkflowNotExistentException if Workflow doesnt exists.
     * @throws IncompleteEleException 
     */
    @Test
    public void addGetWorkflowTest() throws WorkflowNotExistentException, IncompleteEleException {
        init();
        li.addWorkflow(w);
        assertTrue(li.getWorkflow(w.getId()) == w);
    }

    /**
     * 
     * @throws WorkflowNotExistentException if Worklow doesnt exists.
     * @throws IncompleteEleException 
     */
    @Test
    public void startWorkflowTest() throws WorkflowNotExistentException, IncompleteEleException {
        init();
        initExtension();

        li.addWorkflow(w);

        li.startWorkflow(w.getId(), user.getUsername()); // added
                                                         // user.getUsername
                                                         // because
                                                         // startWorkflow
                                                         // expects String not
                                                         // user

        assertFalse(w.getItems().isEmpty());
    }

    /**
     * 
     * @throws WorkflowNotExistentException if Worklow doesnt exists.
     * @throws IncompleteEleException 
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void deleteWortflowTest() throws WorkflowNotExistentException, IncompleteEleException {
        init();
        li.addWorkflow(w);
        li.deleteWorkflow(w.getId());
        
        li.getWorkflow(w.getId());

    }

    /**
     * 
     * @throws WorkflowNotExistentException if Worklow doesnt exists.
     * @throws ItemNotExistentException if Item doesnt exists.
     * @throws UserNotExistentException if User doesnt exists.
     * @throws UserHasNoPermissionException 
     * @throws ItemNotForwardableException 
     * @throws IncompleteEleException 
     */
    @Test
    public void stepOverTest() throws WorkflowNotExistentException,
            ItemNotExistentException, UserNotExistentException, ItemNotForwardableException, UserHasNoPermissionException, IncompleteEleException 
    {
        init();
        initExtension();
        li.addWorkflow(w);
        li.startWorkflow(w.getId(), user.getUsername());
        li.stepForward(w.getItemByPos(0).getId(), w.getStepById(action.getId())
                .getId(), user.getUsername());
        li.stepForward(w.getItemByPos(0).getId(), w.getStepById(action.getId())
                .getId(), user.getUsername());
        assertTrue(w.getItemByPos(0).getStepState(action.getId()) == "DONE");
    }

    /**
     * 
     * @throws WorkflowNotExistentException if Worklow doesnt exists.
     * @throws IncompleteEleException 
     */
    @Test
    public void addStepTest() throws WorkflowNotExistentException, IncompleteEleException {
        init();
        li.addWorkflow(w);
        final int i = w.getSteps().size();
        li.addStep(w.getId(), new Action());
        assertTrue(w.getSteps().size() == i + 1);

    }

    /**
     * 
     * @throws WorkflowNotExistentException if Worklow doesnt exists.
     * @throws IncompleteEleException 
     */
    @Test
    public void deleteStepTest() throws WorkflowNotExistentException, IncompleteEleException {
        init();
        li.addWorkflow(w);
        final int i = w.getSteps().size();
        li.deleteStep(w.getId(), action.getId());
        assertTrue(w.getSteps().size() == i - 1);

    }

    /**
     * 
     * @throws UserNotExistentException if User doesnt exists.
     */
    @Test
    public void addGetUserTest() throws UserNotExistentException {
        init();
        try {
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
        assertTrue(li.getUser(user.getUsername()) == user);
    }

    /**
     * 
     * @throws UserAlreadyExistsException if User Already exists in persistence.
     */
    @Test(expected = UserAlreadyExistsException.class)
    public void userAlreadyExistsTest() throws UserAlreadyExistsException {
        init();
        li.addUser(user);
        li.addUser(user);

    }

    /**
     * 
     * @throws UserNotExistentException if User doesnt exists.
     */
    @Test(expected = UserNotExistentException.class)
    public void deleteUserTest() throws UserNotExistentException {
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
     */
    @Test
    public void getWorkflowsByUser() throws LogicException, WorkflowNotExistentException, UserNotExistentException {
        init();
        initExtension();

        final int i = li.getAllWorkflowsByUser(user.getUsername()).size();
        assertTrue(i == 1);

    }

    /**
     * 
     * @throws WorkflowNotExistentException if Worklow doesnt exists.
     * @throws UserNotExistentException if User doesnt exists.
     */
    @Test
    public void getOpenItemsByUserTest() throws WorkflowNotExistentException,
            UserNotExistentException 
    {
        init();
        initExtension();

        li.startWorkflow(w.getId(), user.getUsername());
        li.startWorkflow(w.getId(), user.getUsername());

        final int i = li.getRelevantItemsByUser(w.getId(), user.getUsername()).size();
        assertTrue(i == 2);

    }
    
    /**
     * Check if steps in a workflow are connected.
     */
    @Test
    public void connectWorkflowSteps() {
        init();
        initExtension();
        
        
        for (Step step: w.getSteps()) {
            
            if (!(step instanceof FinalStep)) {
                System.out.println(step.getId());
                assertTrue(step.getNextSteps().size() != 0);
            }
        }
    }

    /**
     * 
     * @throws LogicException if something went wrong in logic.
     * @throws WorkflowNotExistentException 
     * @throws UserNotExistentException 
     */
    @Test
    public void getStartableWorkflowsTest() throws LogicException, UserNotExistentException, WorkflowNotExistentException {
        init();
        initExtension();
        final int i = li.getStartableWorkflowsByUser(user2.getUsername()).size();
        assertTrue(i == 1);
    }

    /**
     * This Method test deactivation.
     * @throws IncompleteEleException 
     */
    @Test
    public void deactivateWorkflow() throws IncompleteEleException {
        init();
        li.addWorkflow(w);
        w.setActive(false);
        assertFalse(w.isActive());
    }
    /**
     * 
     * @throws WorkflowNotExistentException if workflow doesnt exists.
     * @throws IncompleteEleException 
     */
    @Test
    public void getAllActiveWorkflowTest() throws WorkflowNotExistentException, IncompleteEleException {
        init();
        final int before = li.getAllWorkflows().size();
        li.addWorkflow(w);
        final int between = li.getAllWorkflows().size();
        li.deactivateWorkflow(w.getId());
        final int after = li.getAllWorkflows().size();

        assertTrue(before == after);
        assertTrue(before + 1 == between);
    }
    
    /**
     * 
     * @throws UserNotExistentException if user doesnt exisis.
     * @throws LogicException if something went wrong in logic.
     * @throws WorkflowNotExistentException 
     */
    @Test
    public void getAllActiveWorkflowsByUserTest() throws UserNotExistentException, LogicException, WorkflowNotExistentException {
        init();
        initExtension();       
        final int before = li.getAllWorkflowsByUser(user.getUsername()).size();
       
        li.deactivateWorkflow(w.getId());
        final int after = li.getAllWorkflowsByUser(user.getUsername()).size();

        assertTrue(before == after + 1);
        
    }
    
    

    /**
     * this method init basic data for testing.
     */
    private void init() {
        final Injector i = Guice.createInjector(new SingleModule());
        li = i.getInstance(Logic.class);

        user = new User();
        user.setUsername("0");

        startStep = new StartStep(user.getUsername());
        action = new Action(user.getUsername(), "description");
        finalStep = new FinalStep();

        w = new Workflow();
        w.addStep(startStep);
        w.addStep(action);
        w.addStep(finalStep);
    }
    
    /**
     * This method init more data for testing.

     */
    private void initExtension() {
        user2 = new User();
        user2.setUsername("2");

        user1 = new User();
        user1.setUsername("1");

        startStep1 = new StartStep(user1.getUsername());
        startStep2 = new StartStep(user2.getUsername());
        action1 = new Action(user1.getUsername(), "description");
        action2 = new Action(user2.getUsername(), "description");
        finalStep = new FinalStep();

        w1 = new Workflow();
        w1.addStep(startStep1);
        w1.addStep(action1);
        w1.addStep(finalStep);

        w2 = new Workflow();
        w2.addStep(startStep2);
        w2.addStep(action2);
        w2.addStep(finalStep);

        w3 = new Workflow();
        w3.addStep(startStep1);
        w3.addStep(action2);
        w3.addStep(new FinalStep());

        try {
            li.addWorkflow(w);
            li.addWorkflow(w1);
            li.addWorkflow(w2);
            li.addWorkflow(w3);

        } catch (IncompleteEleException e1) {
            e1.printStackTrace();
        }
        
        try {
            li.addUser(user2);
            li.addUser(user1);
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
    }

}
