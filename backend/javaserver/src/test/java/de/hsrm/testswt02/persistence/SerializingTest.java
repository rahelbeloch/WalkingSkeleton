package de.hsrm.testswt02.persistence;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.persistence.exceptions.FormNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

public class SerializingTest {

    static Persistence db;
    static Properties p;
    static UseLogger ul;
    
    @BeforeClass
    public static void setUp() {
        ul = new UseLogger();
        db = new PersistenceImp(ul);
        p = new Properties();
        p.setProperty("StoragePath", "serializingTest.ser");
        db.setPropConfig(p);
    }
    
    @Test(expected = UserNotExistentException.class)
    public void testUserSerialization() throws PersistenceException {
        db.load();
        
        List<User> list = db.loadAllUsers();
        for (User ele: list) {
            System.out.println(ele);
        }
        
        final User newUser = new User();
        newUser.setUsername("Alpha");
        db.storeUser(newUser);
        
        db.save();
        db.load();
        
        assertTrue(db.loadAllUsers().contains(newUser));
        
        db.deleteUser(newUser.getUsername());
        
        db.save();
        db.load();
        
        db.loadUser(newUser.getUsername());
    }
    
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowSerialization() throws PersistenceException {
        db.load();
        
        List<Workflow> list = db.loadAllWorkflows();
        for (Workflow ele: list) {
            System.out.println(ele);
        }
            
        final Workflow wf = new Workflow();
        db.storeWorkflow(wf);
        
        db.save();
        db.load();
        
        assertTrue(db.loadAllWorkflows().contains(wf));
        
        db.deleteWorkflow(wf.getId());
        
        db.save();
        db.load();
        
        db.loadWorkflow(wf.getId());
    }
    
    @Test(expected = RoleNotExistentException.class)
    public void testRoleSerialization() throws PersistenceException {
        db.load();
        
        List<Role> list = db.loadAllRoles();
        for (Role ele: list) {
            System.out.println(ele);
        }
            
        final Role r = new Role();
        db.storeRole(r);
        
        db.save();
        db.load();
        
        assertTrue(db.loadAllRoles().contains(r));
        
        db.deleteRole(r.getId());
        
        db.save();
        db.load();
        
        db.loadRole(r.getId());
    }
    
    @Test(expected = FormNotExistentException.class)
    public void testFormSerialization() throws PersistenceException {
        db.load();
        
        List<Form> list = db.loadAllForms();
        for (Form ele: list) {
            System.out.println(ele);
        }
            
        final Form f = new Form();
        db.storeForm(f);
        
        db.save();
        db.load();
        
        assertTrue(db.loadAllForms().contains(f));
        
        db.deleteForm(f.getId());
        
        db.save();
        db.load();
        
        db.loadForm(f.getId());
    }

}
