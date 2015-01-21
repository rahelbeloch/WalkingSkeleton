package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.workflowValidator.WorkflowValidator;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneActionException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneFinalStepException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;

/**
 * This TestClass tests the logic interface.
 *
 */
public class WorkflowValidatorTest {

    private WorkflowValidator validator;
    static Persistence db;
    static UseLogger ul;
    private static Role r;
    
    static Workflow workflow;
    static Step ss;
    static Step a1;
    static Step a2;
    static Step a3;
    static Step fs;
    
    /**
     * @throws PersistenceException 
     * 
     */
    @BeforeClass
    public static void setUp() throws PersistenceException {
        ul = new UseLogger();
        db = ConstructionFactory.getTestInstance().getLogic().getPersistence();
        
        r = new Role();
        r.setRolename("TestRolle");
        db.storeRole(r);
        
        workflow = new Workflow();
        ss = new StartStep();
        ss.addRole(r.getRolename());
        a1 = new Action();
        a1.addRole(r.getRolename());
        a2 = new Action();
        a2.addRole(r.getRolename());
        a3 = new Action();
        a3.addRole(r.getRolename());
        fs = new FinalStep();
        fs.addRole(r.getRolename());
        
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
        validator = new WorkflowValidator(workflow, db);
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
