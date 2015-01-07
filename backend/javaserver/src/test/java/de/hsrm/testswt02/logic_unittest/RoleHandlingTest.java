package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.User;
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
    public void DeletionOfRolesTest() throws PersistenceException {
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
        
        li.addRoleToUser(boss.getUsername(), chief);
        li.addRoleToUser(boss.getUsername(), employee);
        
        li.addRoleToUser(assistant.getUsername(), handkerchief);
        li.addRoleToUser(assistant.getUsername(), employee);
        
        li.deleteRole("employee");
        
        assertEquals(li.getUser(boss.getUsername()).getRoles().size(), 1);
    }
}