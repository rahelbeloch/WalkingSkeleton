package de.hsrm.swt02.businesslogic.workflowValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.python.antlr.ast.boolopType;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneActionException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneFinalStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedOneStartStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.InvalidFinalStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.InvalidPythonSyntaxException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.InvalidWorkflowException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.UnreachableStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.WorkflowCyclesException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.WorkflowMustTerminateException;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Fork;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;

/**
 * This class offers all needed methods to validate an incoming workflow.
 */
public class WorkflowValidator {

    private Workflow workflow;
    private Persistence persistence;
    
    final private ScriptEngineManager sm = new ScriptEngineManager();
    final private ScriptEngine sEngine = sm.getEngineByName("jython");
    
    /**
     * Constructor for WorkflowValidator.
     * 
     * @param workflow to validate
     * @param persistence 
     */
    public WorkflowValidator(Workflow workflow, Persistence persistence) {
        this.workflow = workflow;
        this.persistence = persistence;
    }

    /**
     * central validation where each component is checked.
     * 
     * @return boolean - returns true if valid and false if not
     * @throws LogicException to catch InvalidWorkflowException and IncompleteEleException
     */
    public boolean isValid() throws LogicException  {
        if (!checkActionsNextSteps()) {
            throw new IncompleteEleException("[validator] invalid number of NextSteps in Action.");
        } else if (!checkForksNextSteps()) {
            throw new IncompleteEleException("[validator] invalid number of NextSteps in Fork.");
        } else if (numberOfStartSteps() != 1) {
            throw new ExpectedOneStartStepException();
        } else if (numberOfFinalSteps() < 1) {
            throw new ExpectedAtLeastOneFinalStepException();
        } else if (numberOfActions() < 1) {
            throw new ExpectedAtLeastOneActionException();
        } else if (hasCycle(getStartStep(), new ArrayList<Step>())) {
            throw new WorkflowCyclesException();
        } else {
            for (Step step : workflow.getSteps()) {
                if (!((step instanceof FinalStep) || (step instanceof Fork)) && !hasRole(step)) { 
                    throw new IncompleteEleException(
                            "Every step must have an assigned role.");
                } else if (!isReachable(getStartStep(), step)) {
                    throw new UnreachableStepException("step " + step.getId()
                            + " is not reachable.");
                }
            }
        }
        checkFinalSteps();
        validatePythonScript();
        return true;
    }

    /**
     * checks number of Actions NextSteps.
     * @return True is amount is valid, false if not
     */
    public boolean checkActionsNextSteps() {
        for (Step step: workflow.getSteps()) {
            if (step instanceof Action) {
                if (step.getNextSteps().size() != 1) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * checks number of Forks NextSteps.
     * @return True is amount is valid, false if not
     */
    public boolean checkForksNextSteps() {
        for (Step step: workflow.getSteps()) {
            if (step instanceof Fork) {
                if (step.getNextSteps().size() != 2) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * checks if a given step has an assigned role.
     * 
     * @param step - step to check
     * @return boolean
     * @throws RoleNotExistentException 
     */
    public boolean hasRole(Step step) throws RoleNotExistentException {
        if (step.getRoleIds().size() > 0) {
            for (String roleId: step.getRoleIds()) {
                try {
                    persistence.loadRole(roleId);
                } catch (PersistenceException e) {
                    throw new RoleNotExistentException("[validator] At least one assigned role is not stored in persistence.");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * checks if there is a cycling sequence in the workflow.
     * 
     * @param actStep - initial step
     * @param passed - List of passed Steps
     * @return boolean
     */
    private boolean hasCycle(Step actStep, List<Step> passed) {
        for (Step next : actStep.getNextSteps()) {
            if (passed.contains(next)) {
                return true;
            } else {
                passed.add(next);
            }
        }
        for (Step next : actStep.getNextSteps()) {
            return hasCycle(next, passed);
        }
        return false;
    }

    /**
     * checks if a certain step is reachable from anywhere in the workflow.
     * 
     * @param actStep - initial Step
     * @param stepToReach - step to check
     * @return boolean
     */
    private boolean isReachable(Step actStep, Step stepToReach) {     
        if (actStep.getNextSteps().contains(stepToReach) || actStep.equals(stepToReach)) {
            return true;
        } else {
            if(actStep.getNextSteps().isEmpty()) {
                return true;
                }
            for (Step s : actStep.getNextSteps()) {
                return isReachable(s, stepToReach);
            }
            return false;
        }
    }

    /**
     * This method checks if Final Steps have any next steps and if steps with
     * no next steps are Final Steps.
     * 
     * @throws InvalidWorkflowException - if the workflow is not valid
     */
    private void checkFinalSteps() throws InvalidWorkflowException {
        for (Step step : workflow.getSteps()) {
            if (step.getNextSteps().isEmpty() && !(step instanceof FinalStep)) {
                throw new WorkflowMustTerminateException("step " + step.getId()
                        + "must be followed by at least one other step.");
            } else if (step instanceof FinalStep
                    && step.getNextSteps().size() > 0) 
            {
                throw new InvalidFinalStepException("Final Step "
                        + step.getId() + "must not have any next steps.");
            }
        }
    }

    /**
     * counts the number of StartSteps in workflow.
     * 
     * @return counter
     */
    private int numberOfStartSteps() {
        int counter = 0;
        for (Step step : workflow.getSteps()) {
            if (step instanceof StartStep) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * counts the number of Actions in workflow.
     * 
     * @return counter
     */
    private int numberOfActions() {
        int counter = 0;
        for (Step step : workflow.getSteps()) {
            if (step instanceof Action) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * counts the number of Final Steps in workflow.
     * 
     * @return counter
     */
    private int numberOfFinalSteps() {
        int counter = 0;
        for (Step step : workflow.getSteps()) {
            if (step instanceof FinalStep) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Getter for the StartStep in workflow.
     * 
     * @return step - the workflows StartStep
     */
    public Step getStartStep() {
        for (Step step : workflow.getSteps()) {
            if (step instanceof StartStep) {
                return step;
            }
        }
        return null;
    }

    private void validatePythonScript() throws InvalidPythonSyntaxException {
    	for(Step step : this.workflow.getSteps()) {
    		if(step instanceof Fork) {
    			Fork fork = (Fork) step;
    			try {
					sEngine.eval(fork.getScript());
					
				} catch (ScriptException e) {
					throw new InvalidPythonSyntaxException();
				}
    			
    			try {
    				Invocable inv = (Invocable) sEngine;
		            inv.invokeFunction("check", new HashMap<String, String>());
    			} catch(NoSuchMethodException e) {
    				throw new InvalidPythonSyntaxException();
    			} catch (ScriptException e) {
    				
    			}
    		}
    	}
    }
}