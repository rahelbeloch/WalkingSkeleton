package de.hsrm.testswt02.persistence;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;

public class SerializingTest {

    static Persistence db;
    static Properties p;
    
    @BeforeClass
    public static void setUp() {
        db = new PersistenceImp(null);
        p = new Properties();
        p.setProperty("StoragePath", "serializingTest");
        db.setPropConfig(p);
    }
    
    @Test
    public void test() throws PersistenceException {
        db.load();
        
        User newUser = new User();
        newUser.setUsername("Alpha");
        db.storeUser(newUser);
        
        Workflow newWorkflow = new Workflow();
        final String id = db.storeWorkflow(newWorkflow);
        
        db.save();
        db.load();
        
        assertTrue(db.loadAllUsers().contains(newUser));
        assertEquals(db.loadWorkflow(id), newWorkflow);
    }
}
