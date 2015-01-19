package de.hsrm.testswt02.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.persistence.exceptions.FormNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * testing class for long-living serialization.
 */
public class SerializingTest {

    static Persistence db;
    static Properties p;
    static UseLogger ul;
    
    static String path = "serializingTest.ser";

    /**
     * setting Up useLogger, persistence and Props before testing.
     * 
     */
    @BeforeClass
    public static void setUp() {
        ul = new UseLogger();
        db = new PersistenceImp(ul);
        db.setStoragePath(path);
    }
    
    /**
     * Cleaning up all created files after testing.
     * 
     */
    @AfterClass
    public static void cleanUp() {
        final File file = new File(path);
        file.delete();
    }

    /**
     * Persistence testing for serialized users.
     * 
     * @throws PersistenceException if an error in Persistence occurs
     */
    @Test(expected = UserNotExistentException.class)
    public void testUserSerialization() throws PersistenceException {
        db.load();

//        final List<User> list = db.loadAllUsers();
//        for (User ele : list) {
//            System.out.println(ele);
//        }

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

    /**
     * Persistence testing for serialized workflows.
     * 
     * @throws PersistenceException if an error in Persistence occurs
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowsContentSerialization() throws PersistenceException {
        db.load();
                
        final Workflow wf = new Workflow();
        final Item testItem1 = new Item();
        final Item testItem2 = new Item();
        final Step testStep1 = new Step();
        final Step testStep2 = new Step();
        final Role role1 = new Role();
        final Role role2 = new Role();
        role1.setRolename("role1");
        testStep1.getRoleIds().add(role1.getRolename());
        role2.setRolename("role2");
        testStep2.getRoleIds().add(role2.getRolename());
        wf.addStep(testStep1);
        wf.addStep(testStep2);
        wf.addItem(testItem1);
        wf.addItem(testItem2);
        db.storeWorkflow(wf);
        
        db.save();
        db.load();

        assertTrue(db.loadAllWorkflows().contains(wf));
        assertEquals(db.loadWorkflow(wf.getId()).getItems(), wf.getItems());
        assertEquals(db.loadWorkflow(wf.getId()).getSteps(), wf.getSteps());
        
        db.deleteWorkflow(wf.getId());

        db.save();
        db.load();

        db.loadWorkflow(wf.getId());
    }

    /**
     * Persistence testing for serialized workflows and their content.
     * 
     * @throws PersistenceException if an error in Persistence occurs
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowSerialization() throws PersistenceException {
        db.load();

//        final List<Workflow> list = db.loadAllWorkflows();
//        for (Workflow ele : list) {
//            System.out.println(ele);
//        }

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

    /**
     * Persistence testing for serialized roles.
     * 
     * @throws PersistenceException if an error in Persistence occurs
     */
    @Test(expected = RoleNotExistentException.class)
    public void testRoleSerialization() throws PersistenceException {
        db.load();

//        final List<Role> list = db.loadAllRoles();
//        for (Role ele : list) {
//            System.out.println(ele);
//        }

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

    /**
     * Persistence testing for serialized forms.
     * 
     * @throws PersistenceException if an error in Persistence occurs
     */
    @Test(expected = FormNotExistentException.class)
    public void testFormSerialization() throws PersistenceException {
        db.load();

//        final List<Form> list = db.loadAllForms();
//        for (Form ele : list) {
//            System.out.println(ele);
//        }

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
