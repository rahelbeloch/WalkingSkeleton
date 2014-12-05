package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicImp;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This class test the logic interface.
 *
 */
public class LogicTest {

    Logic li;
    Workflow w;
    Workflow w1;
    Workflow w2;
    Workflow w3;
    final int workflowID = 3;
    String username = "noname";
    User user;
    User user1;
    User user2;

    /**
     * test add and get workflow in persistence.
     * @throws WorkflowNotExistentException .
     */
    @Test
    public void addGetWorkflowTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        assertTrue(li.getWorkflow(workflowID) == w);
    }

    /**
     * test starts a workflow.
     * @throws WorkflowNotExistentException .
     */
    @Test
    public void startWorkflowTest() throws WorkflowNotExistentException {
        init();

        li.addWorkflow(w);


        li.startWorkflow(workflowID, user.getUsername()); // added user.getUsername because startWorkflow expects String not user


        assertFalse(w.getItems().isEmpty());
    }

    /**
     * test delete Workflow in persistence.
     * @throws WorkflowNotExistentException .
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void deleteWortflowTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        li.deleteWorkflow(workflowID);
        assertTrue(li.getWorkflow(workflowID) == null);

    }

    /**
     * test handle a step.
     * @throws WorkflowNotExistentException .
     * @throws ItemNotExistentException .
     * @throws UserNotExistentException .
     */
    @Test
    public void stepOverTest() throws WorkflowNotExistentException, ItemNotExistentException, UserNotExistentException {
        init();
        final int c = 100;
        initExtension();
        li.addWorkflow(w);

        li.startWorkflow(workflowID, user.getUsername());
        li.stepForward(w.getItemByPos(0).getId(), w.getStepById(workflowID * c).getId(), user.getUsername());
        li.stepForward(w.getItemByPos(0).getId(), w.getStepById(workflowID * c).getId(), user.getUsername());

        assertTrue(w.getItemByPos(0).getStepState(workflowID * c) == "DONE");
    }

    /**
     * test add Action into an existing workflow.
     * @throws WorkflowNotExistentException .
     */
    @Test
    public void addStepTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        final int i = w.getSteps().size();
        li.addStep(workflowID, new Action());
        assertTrue(w.getSteps().size() == i + 1);

    }

    /**
     * test delete Step.
     * @throws WorkflowNotExistentException .
     */
    @Test
    public void deleteStepTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        final int c = 100; 
        final int i = w.getSteps().size();
        li.deleteStep(workflowID, workflowID * c);
        assertTrue(w.getSteps().size() == i - 1);

    }

    /**
     * test add and get an User in persistence.
     * @throws UserNotExistentException . 
     */
    @Test
    public void addGetUserTest() throws UserNotExistentException {
        init();
        try {
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
        assertTrue(li.getUser(username) == user);
    }

    /**
     * test whether an User exists in persistence.
     * @throws UserAlreadyExistsException .
     */
    @Test(expected = UserAlreadyExistsException.class)
    public void userAlreadyExistsTest() throws UserAlreadyExistsException {
        init();
        li.addUser(user);
        li.addUser(user);

    }

    /**
     * test delete an User in persistence. 
     * @throws UserNotExistentException . 
     */
    @Test(expected = UserNotExistentException.class)
    public void deleteUserTest() throws UserNotExistentException {
        init();
        try {
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
        li.deleteUser(username);
        assertTrue(li.getUser(username) == null);
    }

    /**
     * test get all workflows in which the user involved.
     */
    @Test
    public void getWorkflowsByUser() {
        init();
        initExtension();
        final int c = 3; 
        assertTrue(li.getWorkflowsByUser(user1.getUsername()).size() == c);

    }

    /**
     * test get all workflows which the user have sth. to do. 
     * @throws WorkflowNotExistentException .
     */
    @Test
    public void getOpenItemsByUserTest() throws WorkflowNotExistentException {
        init();
        initExtension();

        li.startWorkflow(workflowID + 1, user1.getUsername());
        li.startWorkflow(workflowID + 2, user1.getUsername());

        assertTrue(li.getOpenItemsByUser(user1.getUsername()).size() == 2);

    }

    /**
     * test get all workflows which the user can start.
     */
    @Test
    public void getStartableWorkflowsTest() {
        init();
        initExtension();
        assertTrue(li.getStartableWorkflows(user2.getUsername()).size() == 2);
    }
    
    

    /**
     * init for tests.
     */
    private void init() {
        final Injector i = Guice.createInjector(new SingleModule());
        li = i.getInstance(Logic.class);
        final int c = 100;
        user = new User();
        user.setUsername(username);

        w = new Workflow();
        w.addStep(new StartStep(user.getUsername()));
        w.addStep(new Action(workflowID * c, user.getUsername(),
                "description"));
        w.addStep(new FinalStep());
        w.setId(workflowID);
    }

    /**
     * init for bigger tests.
     */
    private void initExtension() {
        
        final int c = 100;
        user2 = new User();
        user2.setUsername("2");

        user1 = new User();
        user1.setUsername("1");

        w1 = new Workflow();
        w1.addStep(new StartStep(user2.getUsername()));
        w1.addStep(new Action(workflowID * c, user1.getUsername(),
                "description"));
        w1.addStep(new FinalStep());
        w1.setId(workflowID + 1);

        w2 = new Workflow();
        w2.addStep(new StartStep(user2.getUsername()));
        w2.addStep(new Action(workflowID * c, user1.getUsername(),
                "description"));
        w2.addStep(new FinalStep());
        w2.setId(workflowID + 2);

        w3 = new Workflow();
        final int stepId = 3;
        w3.addStep(new StartStep(user1.getUsername()));
        w3.addStep(new Action(workflowID * c, user2.getUsername(),
                "description"));
        w3.addStep(new FinalStep());
        w3.setId(workflowID + stepId);

        li.addWorkflow(w);
        li.addWorkflow(w1);
        li.addWorkflow(w2);
        li.addWorkflow(w3);

        try {
            li.addUser(user2);
            li.addUser(user1);
            li.addUser(user);
        } catch (UserAlreadyExistsException e) {
            assertTrue(false);
        }
    }

}
