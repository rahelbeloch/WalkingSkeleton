package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.businesslogic.WorkflowValidator;
import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;


/**
 * This Testclass tests the logic interface.
 *
 */
public class WorkflowValidatorTest {

    private WorkflowValidator validator;
    private PersistenceImp db;
    private static Role r = new Role();
    
    @BeforeClass
    public static void setUp() {
       r.setRolename("TestRolle"); 
    }
    
    @Test
    public void testWorkflowValidation() throws PersistenceException, IncompleteEleException {
        Workflow workflow = new Workflow();
        
        StartStep ss = new StartStep();
        Action a1 = new Action();
        FinalStep fs = new FinalStep();
        
        ss.addRole(r.getRolename());
        a1.addRole(r.getRolename());
        fs.addRole(r.getRolename());
        
        workflow.addStep(ss);
        workflow.addStep(a1);
        ss.getNextSteps().add(a1);
        workflow.addStep(fs);
        a1.getNextSteps().add(fs);
        
        validator = new WorkflowValidator(workflow);
        assertTrue(validator.isValid());
    }
    
}
