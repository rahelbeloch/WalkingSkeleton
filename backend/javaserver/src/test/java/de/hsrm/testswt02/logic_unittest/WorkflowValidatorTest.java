package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.workflowValidator.WorkflowValidator;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneActionException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneFinalStepException;
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
    
    static Workflow workflow;
    static StartStep ss;
    static Action a1;
    static Action a2;
    static Action a3;
    static FinalStep fs;
    
    /**
     * 
     */
    @BeforeClass
    public static void setUp() {
        r.setRolename("TestRolle");
        workflow = new Workflow();
        ss = new StartStep();
        a1 = new Action();
        a2 = new Action();
        a3 = new Action();
        fs = new FinalStep();
        
        ss.addRole(r.getRolename());
        a1.addRole(r.getRolename());
        a2.addRole(r.getRolename());
        a3.addRole(r.getRolename());
        fs.addRole(r.getRolename());
    }
    
    /**
     * 
     * @throws LogicException to catch InvalidWorkflowException and IncompleteEleException
     */
    private void isValid() throws LogicException {
        validator = new WorkflowValidator(workflow);
        assertTrue(validator.isValid());
    }
    
    /**
     * 
     */
    private void clearWorkflow() {
        workflow = new Workflow();
    }
    
    /**
     * 
     * @throws LogicException to catch InvalidWorkflowException and IncompleteEleException
     */
    @Test
    public void testWorkflowValidation() throws LogicException {
        clearWorkflow();
        workflow.addStep(ss);
        workflow.addStep(a1);
        workflow.addStep(fs);
        
        isValid();
    }
    
    /**
     * 
     * @throws LogicException to catch InvalidWorkflowException and IncompleteEleException
     */
    @Test(expected = ExpectedAtLeastOneFinalStepException.class)
    public void testMinOneFinalStep() throws LogicException {
        clearWorkflow();
        workflow.addStep(ss);
        workflow.addStep(a1);
        
        isValid();
    }
    
    /**
     * 
     * @throws LogicException to catch InvalidWorkflowException and IncompleteEleException
     */
    @Test(expected = ExpectedAtLeastOneActionException.class)
    public void testMinOneAction() throws LogicException {
        clearWorkflow();
        workflow.addStep(ss);
        workflow.addStep(fs);
        
        isValid();
    }
}
