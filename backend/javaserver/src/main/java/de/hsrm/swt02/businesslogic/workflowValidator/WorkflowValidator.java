package de.hsrm.swt02.businesslogic.workflowValidator;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneActionException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedAtLeastOneFinalStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.ExpectedOneStartStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.InvalidFinalStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.InvalidWorkflowException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.UnreachableStepException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.WorkflowCyclesException;
import de.hsrm.swt02.businesslogic.workflowValidator.exceptions.WorkflowMustTerminateException;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;

/**
 * This class offers all needed methods to validate an incoming workflow.
 */
public class WorkflowValidator {
    
    private Workflow workflow;
    
    public WorkflowValidator (Workflow workflow) {
        this.workflow = workflow;
    }

    public boolean isValid() throws InvalidWorkflowException {
        if (numberOfStartSteps() != 1) {
            throw new ExpectedOneStartStepException();
        } else if (numberOfFinalSteps() <1) {
            throw new ExpectedAtLeastOneFinalStepException();
        } else if (numberOfActions() <1) {
            throw new ExpectedAtLeastOneActionException();
        } else if (hasCycle(getStartStep(), new ArrayList<Step>())) {
            throw new WorkflowCyclesException();
        } else {
            for (Step step: workflow.getSteps()) {
                if (!isReachable(getStartStep(), step)) {
                    throw new UnreachableStepException("step " + step.getId() + "is not reachable.");
                }
            }
        }
        checkFinalSteps();
        return true;
    }
    
    private boolean hasCycle(Step actStep, List<Step> passed) {
        for (Step next: actStep.getNextSteps()) {
            if (passed.contains(next)) {
                return true;
            } else {
                passed.add(next);
            }
        }
        for (Step next: actStep.getNextSteps()) {
            return hasCycle(next, passed);
        }
    return false;
    }
    
    private boolean isReachable (Step actStep,Step stepToReach) {
        if (actStep.getNextSteps().contains(stepToReach) || actStep.equals(stepToReach)) {
            return true;
        } else {
            for(Step s: actStep.getNextSteps()) {
                return isReachable(s, stepToReach);
            }
            return false;
        }
    }
    
    private void checkFinalSteps() throws InvalidWorkflowException {
        for(Step step: workflow.getSteps()) {
            if (step.getNextSteps().isEmpty() && !(step instanceof FinalStep)) {
                throw new WorkflowMustTerminateException("step " + step.getId() + "must be followed by at least one other step.");
            } else if (step instanceof FinalStep && step.getNextSteps().size() > 0) {
                throw new InvalidFinalStepException("Final Step " + step.getId() + "must not have any next steps.");
            }
        }
    }
    
    private int numberOfStartSteps() {
        int counter = 0;
        for(Step step: workflow.getSteps()) {
            if(step instanceof StartStep) {
                counter++;
            }
        }
        return counter;
    }
    
    private int numberOfActions() {
        int counter = 0;
        for(Step step: workflow.getSteps()) {
            if(step instanceof Action) {
                counter++;
            }
        }
        return counter;
    }

    
    private int numberOfFinalSteps () {
        int counter = 0;
        for(Step step: workflow.getSteps()) {
            if(step instanceof FinalStep) {
                counter++;
            }
        }
        return counter;
    }
    
    public Step getStartStep() {
        for(Step step: workflow.getSteps()) {
            if(step instanceof StartStep) {
                return step;
            }
        }
        return null;
    }
    
}