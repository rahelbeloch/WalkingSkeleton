package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;

/**
 * This Class tests the initialization of a workflow with Steps.
 *
 */
public class FormHandlingTest {

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
    
    /**
     * Test if forms can be properly deleted.
     * @throws LogicException 
     */
    @Test
    public void deletionOfFormsTest() throws LogicException {
        final int sizeBefore = li.getAllForms().size();
        final Form form1 = new Form();
        form1.setId("1");
        final Form form2 = new Form();
        form2.setId("2");
        
        li.addForm(form1);
        li.addForm(form2);
                        
        li.deleteForm(form1.getId());
        
        assertEquals(li.getAllForms().size(), sizeBefore + 1);
    }

    /**
     * Test if Forms can be properly stored.
     * @throws PersistenceException 
     */
    @Test
    public void testFormStorage() throws PersistenceException {    
        final Form form1 = new Form();
        form1.setId("form1");
        final Form form2 = new Form();
        form2.setId("form2");
        li.addForm(form1);
        li.addForm(form2);
        
        assertEquals(form2.getId(), li.getForm(form2.getId()).getId());
        assertEquals(form1.getId(), li.getForm(form1.getId()).getId());
    }
    
    /**
     * Test if a form can be stored twice. Will fail if it can.
     * @throws PersistenceException 
     */
    @Test
    public void testDuplicateFormStorage() throws PersistenceException {
        final int sizeBefore = li.getAllForms().size();
        final Form form1 = new Form();
        form1.setId("1");
        li.addForm(form1);
        
        final Form form2 = new Form();
        form2.setId("1");
        li.addForm(form2);
        
        assertEquals(li.getAllForms().size(), sizeBefore + 1);
    }
    
    /**
     * Test if a form can be deleted whilst being in use.
     * @throws LogicException 
     */
    @Test
    public void deletionOfFormsStillInUse() throws LogicException {
        // TODO: not implemented yet
    }
    
    
}