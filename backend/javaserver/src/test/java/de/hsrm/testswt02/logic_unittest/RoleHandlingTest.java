package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.NoPermissionException;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;

/**
 * This Class tests the initialization of a workflow with Steps.
 *
 */
public class RoleHandlingTest {

    // Dependency Injection
    Injector inj = Guice.createInjector(new SingleModule());
    Logic li = inj.getInstance(Logic.class);
    
    /**
     * configurate Logger in order to get Logging output.
     */
    @BeforeClass
    public static void setup() {
        LogConfigurator.setup();
    }
    
    @Test
    public void RoleStorageTest() throws PersistenceException {    
        Role role1 = new Role();
        role1.setRolename("chief");
        Role role2 = new Role();
        role2.setRolename("handkerchief");
        li.addRole(role1);
        li.addRole(role2);
        
        assertEquals(role2.getRolename(), li.getRole(role2.getRolename()).getRolename());
        assertEquals(role1, li.getRole(role1.getRolename()));
    }
    
    @Test
    public void GiveRoleOnlyOnce() throws PersistenceException {
        Role employee = new Role();
        employee.setRolename("employee");
        li.addRole(employee);
        
        User testuser = new User();
        testuser.setUsername("testuser");
        li.addUser(testuser);
        
        li.addRoleToUser(testuser, employee);
        li.addRoleToUser(testuser, employee);
        
        assertEquals(li.getUser(testuser.getUsername()).getRoles().size(), 1);
    }
    
    @Test
    public void DeletionOfRolesTest() throws LogicException {
        Role chief = new Role();
        chief.setRolename("chief");
        Role handkerchief = new Role();
        handkerchief.setRolename("handkerchief");
        Role employee = new Role();
        employee.setRolename("employee");
        
        li.addRole(chief);
        li.addRole(handkerchief);
        li.addRole(employee);
        
        User boss = new User();
        boss.setUsername("boss");
        User assistant = new User();
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
    
    @Test(expected = NoPermissionException.class)
    public void DeletionOfRoleStillInUse() throws LogicException {
        Role chief = new Role();
        chief.setRolename("chief");
        Role handkerchief = new Role();
        handkerchief.setRolename("handkerchief");
        Role employee = new Role();
        employee.setRolename("employee");
        
        li.addRole(chief);
        li.addRole(handkerchief);
        li.addRole(employee);
        
        User boss = new User();
        boss.setUsername("boss");
        User assistant = new User();
        assistant.setUsername("assistant");
        
        li.addUser(boss);
        li.addUser(assistant);
        
        li.addRoleToUser(boss, chief);
        li.addRoleToUser(boss, employee);
        
        li.addRoleToUser(assistant, handkerchief);
        li.addRoleToUser(assistant, employee);
        
        StartStep ss = new StartStep();
        ss.getRoleIds().add(employee.getRolename());
        
        Action action = new Action();
        action.setDescription("Erste Action");
        action.getRoleIds().add(employee.getRolename());
        
        FinalStep finalStep = new FinalStep();
        
        Workflow workflow = new Workflow();
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