package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
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

public class LogicTest {

    Logic li;
    Workflow w;
    Workflow w1;
    Workflow w2;
    Workflow w3;
    int key = 3;
    String username = "noname";
    User user;
    User user1;
    User user2;

    @Test
    public void addGetWorkflowTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        assertTrue(li.getWorkflow(w.getId()) == w);
    }

    @Test
    public void startWorkflowTest() throws WorkflowNotExistentException {
        init();
        initExtension();

        li.addWorkflow(w);

        li.startWorkflow(w.getId(), user.getUsername()); // added user.getUsername because startWorkflow expects String not user

        assertFalse(w.getItems().isEmpty());
    }

    @Test(expected = WorkflowNotExistentException.class)
    public void deleteWortflowTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        li.deleteWorkflow(w.getId());
        assertTrue(li.getWorkflow(w.getId()) == null);

    }

    @Test
    public void stepOverTest() throws WorkflowNotExistentException, ItemNotExistentException, UserNotExistentException {
        init();
        initExtension();
        li.addWorkflow(w);
        li.startWorkflow(w.getId(), user.getUsername());
        li.stepForward(w.getItemByPos(0).getId(), w.getStepById(key * 100).getId(), user.getUsername());
        li.stepForward(w.getItemByPos(0).getId(), w.getStepById(key * 100).getId(), user.getUsername());
        assertTrue(w.getItemByPos(0).getStepState(key * 100) == "DONE");
    }

    @Test
    public void addStepTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        int i = w.getSteps().size();
        li.addStep(w.getId(), new Action());
        assertTrue(w.getSteps().size() == i + 1);

    }

    @Test
    public void deleteStepTest() throws WorkflowNotExistentException {
        init();
        li.addWorkflow(w);
        int i = w.getSteps().size();
        li.deleteStep(w.getId(), key * 100);
        assertTrue(w.getSteps().size() == i - 1);

    }

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

    @Test(expected = UserAlreadyExistsException.class)
    public void userAlreadyExistsTest() throws UserAlreadyExistsException {
        init();
        li.addUser(user);
        li.addUser(user);

    }

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

    @Test
    public void getWorkflowsByUser() throws WorkflowNotExistentException, UserNotExistentException {
        init();
        initExtension();
        int i = li.getWorkflowsByUser(user1.getUsername()).size();
        assertTrue(i == 3);

    }

    @Test
    public void getOpenItemsByUserTest() throws WorkflowNotExistentException, UserNotExistentException {
        init();
        initExtension();

        li.startWorkflow(w.getId(), user.getUsername());
        li.startWorkflow(w.getId(), user.getUsername());
        
        int i = li.getOpenItemsByUser(user.getUsername()).size();
        assertTrue(i == 2);

    }

    @Test
    public void getStartableWorkflowsTest() throws WorkflowNotExistentException, UserNotExistentException {
        init();
        initExtension();
        int i = li.getStartableWorkflows(user2.getUsername()).size();
        assertTrue(i == 2);
    }

    private void init() {
    	Injector i = Guice.createInjector(new SingleModule());
    	li = i.getInstance(Logic.class);

        user = new User();
        user.setUsername(username);

        w = new Workflow();
        w.addStep(new StartStep(user.getUsername()));
        w.addStep(new Action(key * 100, user.getUsername(),
                "description"));
        w.addStep(new FinalStep());
    }

    private void initExtension() {
        user2 = new User();
        user2.setUsername("2");

        user1 = new User();
        user1.setUsername("1");
        

        w1 = new Workflow();
        w1.addStep(new StartStep(user2.getUsername()));
        w1.addStep(new Action(key * 100, user1.getUsername(),
                "description"));
        w1.addStep(new FinalStep());

        w2 = new Workflow();
        w2.addStep(new StartStep(user2.getUsername()));
        w2.addStep(new Action(key * 100, user1.getUsername(),
                "description"));
        w2.addStep(new FinalStep());

        w3 = new Workflow();
        w3.addStep(new StartStep(user1.getUsername()));
        w3.addStep(new Action(key * 100, user2.getUsername(),
                "description"));
        w3.addStep(new FinalStep());

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
