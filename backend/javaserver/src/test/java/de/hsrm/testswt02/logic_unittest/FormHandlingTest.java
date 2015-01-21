package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

/**
 * This Class tests the initialization of a workflow with Steps.
 *
 */
public class FormHandlingTest {

    static Logic li = ConstructionFactory.getTestInstance().getLogic();
    
    /**
     * configurate Logger in order to get Logging output.
     * @throws LogicException 
     */
    @BeforeClass
    public static void setup() throws LogicException {
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
        form1.setId("198765412345654824");
        final Form form2 = new Form();
        form2.setId("248512357154712154");
        
        li.addForm(form1);
        li.addForm(form2);
                        
        li.deleteForm(form2.getId());

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
    @Test(expected = StorageFailedException.class)
    public void deletionOfFormsStillInUse() throws LogicException {    
        final int sizeBefore = li.getAllForms().size();
        //TestForms
        final Form form1 = new Form();
        form1.setId("1984");
        final Form form2 = new Form();
        form2.setId("2014");
        // TestRollen
        final Role r = new Role();
        r.setRolename("TestRolle");
        li.addRole(r);
        // TestWorkflows
        final Workflow wf = new Workflow();
        wf.setForm(form1);
        final Step ss = new StartStep();
        final Step a = new Action();
        final Step fs = new FinalStep();
        ss.addRole(r.getRolename());
        a.addRole(r.getRolename());
        fs.addRole(r.getRolename());
        wf.addStep(ss);
        wf.addStep(a);
        wf.addStep(fs);
        li.addWorkflow(wf);
        
        li.addForm(form1);
        li.addForm(form2);
                        
        li.deleteForm(form1.getId());

        assertEquals(li.getAllForms().size(), sizeBefore + 1);
    }
    
    
}