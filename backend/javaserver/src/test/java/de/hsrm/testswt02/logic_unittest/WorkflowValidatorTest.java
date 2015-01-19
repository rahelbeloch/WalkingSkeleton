package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.workflowValidator.WorkflowValidator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Workflow;

/**
 * This TestClass tests the logic interface.
 *
 */
public class WorkflowValidatorTest {

    private WorkflowValidator validator;
    private static Role r = new Role();
    
    /**
     * 
     */
    @BeforeClass
    public static void setUp() {
        r.setRolename("TestRolle");
    }
    
    /**
     * 
     * @throws LogicException to catch InvalidWorkflow and IncompleteEleException
     */
    @Test
    public void testWorkflowValidation() throws LogicException {
        final Workflow workflow = new Workflow();
        
        final StartStep ss = new StartStep();
        final Action a1 = new Action();
        final FinalStep fs = new FinalStep();
        
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
