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
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

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
    
    @Test
    public void testFormStorage() throws PersistenceException {    
        Form form1 = new Form();
        form1.setId("form1");;
        Form form2 = new Form();
        form2.setId("form2");
        li.addForm(form1);
        li.addForm(form2);
        
        assertEquals(form2.getId(), li.getForm(form2.getId()).getId());
        assertEquals(form1, li.getForm(form1.getId()));
    }
    
    @Test
    public void testDuplicateFormStorage() throws PersistenceException {
        Form form1 = new Form();
        form1.setId("1");
        li.addForm(form1);
        
        Form form2 = new Form();
        form2.setId("1");
        li.addForm(form2);
        
        assertEquals(li.getAllForms().size(), 1);
    }
    
    @Test
    public void DeletionOfFormsTest() throws LogicException {
        Form form1 = new Form();
        form1.setId("1");
        Form form2 = new Form();
        form2.setId("2");;
        
        li.addForm(form1);
        li.addForm(form2);
                        
        li.deleteForm(form1.getId());
        
        assertEquals(li.getAllForms().size(), 1);
    }
    
    @Test(expected = NoPermissionException.class)
    public void DeletionOfFormsStillInUse() throws LogicException {
        // TODO: not implemented yet
    }
    
    
}