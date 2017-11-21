package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.NoPermissionException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

/**
 * This Class tests the initialization of a workflow with Steps.
 *
 */
public class RoleHandlingTest {
    
    static Logic li;
    /**
     * configurate Logger in order to get Logging output.
     */
    @BeforeClass
    public static void setup() {
        LogConfigurator.setup();
        li = ConstructionFactory.getTestInstance().getLogic();
    }
    
    /**
     * Test if storing a step without a role will run into a StorageFailedException.
     * @throws LogicException 
     */
    @Test(expected = StorageFailedException.class)
    public void testStepWithoutRole() throws LogicException {
        final Workflow wf = new Workflow();
        final Step startStep = new StartStep();
        wf.addStep(startStep);
        li.addWorkflow(wf);
    }
    
    /**
     * Test if roles can be stored properly.
     * @throws PersistenceException 
     */
    @Test
    public void roleStorageTest() throws PersistenceException {    
        final Role role1 = new Role();
        role1.setRolename("chief");
        final Role role2 = new Role();
        role2.setRolename("handkerchief");
        li.addRole(role1);
        li.addRole(role2);
        
        assertEquals(role2.getRolename(), li.getRole(role2.getRolename()).getRolename());
        assertEquals(role1, li.getRole(role1.getRolename()));
    }
    
    /**
     * Test if roles can only be assigned once, will fail if not.
     * @throws LogicException if an error in business logic occurs  
     */
    @Test
    public void giveRoleOnlyOnce() throws LogicException {
        final Role employee = new Role();
        employee.setRolename("employee");
        li.addRole(employee);
        
        final User testuser = new User();
        testuser.setUsername("testuser");
        li.addUser(testuser);
        
        li.addRoleToUser(testuser, employee);
        li.addRoleToUser(testuser, employee);
        
        assertEquals(li.getUser(testuser.getUsername()).getRoles().size(), 1);
    }
    
    /**
     * Tests if roles can be deleted.
     * @throws LogicException 
     */
    @Test
    public void deletionOfRolesTest() throws LogicException {
        final Role chief = new Role();
        chief.setRolename("chief");
        final Role handkerchief = new Role();
        handkerchief.setRolename("handkerchief");
        final Role employee = new Role();
        employee.setRolename("employee");
        
        
        li.addRole(chief);
        li.addRole(handkerchief);
        li.addRole(employee);
        
        final User boss = new User();
        boss.setUsername("boss");
        final User assistant = new User();
        assistant.setUsername("assistant");
        
        li.addUser(boss);
        li.addUser(assistant);
        
        li.addRoleToUser(boss, chief);
        li.addRoleToUser(boss, employee);
        
        li.addRoleToUser(assistant, handkerchief);
        li.addRoleToUser(assistant, employee);
        
        li.deleteRole(employee.getRolename());
        
        assertEquals(li.getUser(boss.getUsername()).getRoles().size(), 1);
    }
    
    /**
     * Tests if the deletion of roles that are still in use runs into a NoPermissionException.
     * @throws LogicException 
     */
    @Test(expected = NoPermissionException.class)
    public void deletionOfRoleStillInUse() throws LogicException {
        final Role chief = new Role();
        chief.setRolename("chief");
        final Role handkerchief = new Role();
        handkerchief.setRolename("handkerchief");
        final Role employee = new Role();
        employee.setRolename("employee");
        
        li.addRole(chief);
        li.addRole(handkerchief);
        li.addRole(employee);
        
        final User boss = new User();
        boss.setUsername("boss");
        final User assistant = new User();
        assistant.setUsername("assistant");
        
        li.addUser(boss);
        li.addUser(assistant);
        
        li.addRoleToUser(boss, chief);
        li.addRoleToUser(boss, employee);
        
        li.addRoleToUser(assistant, handkerchief);
        li.addRoleToUser(assistant, employee);
        
        final StartStep ss = new StartStep();
        ss.getRoleIds().add(employee.getRolename());
        
        final Action action = new Action();
        action.setDescription("Erste Action");
        action.getRoleIds().add(employee.getRolename());
        
        final FinalStep finalStep = new FinalStep();
        finalStep.getRoleIds().add(employee.getRolename());
        
        final Workflow workflow = new Workflow();
        workflow.addStep(ss);
        workflow.addStep(action);
        workflow.addStep(finalStep);
//      even if a role is part of a deactivated workflow the role cannot be deleted
//      workflow.setActive(false);
        
        li.addWorkflow(workflow);
        
        assertEquals(li.getWorkflow(workflow.getId()), workflow);
        assertEquals(li.getRole(employee.getRolename()), employee);
        assertEquals(li.getWorkflow(workflow.getId()).getStepById(ss.getId()).getRoleIds(), ss.getRoleIds());
        
        li.deleteRole(employee.getRolename());
    }
}