package de.hsrm.swt02.businesslogic;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
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

    public boolean isValid() throws IncompleteEleException {
        if (numberOfStartSteps() != 1) {
            throw new IncompleteEleException("Expected exactely one StartStep.");
        } else if (numberOfFinalSteps() <1) {
            throw new IncompleteEleException("Expected at least one FinalStep.");
        } else if (numberOfActions() <1) {
            throw new IncompleteEleException("Expected at least one Action expected.");
        } else if (!checkFinalSteps()) {
            throw new IncompleteEleException("Final Steps must not have next steps!");
        } else if (hasCycle(getStartStep(), new ArrayList<Step>())) {
            throw new IncompleteEleException("Found an invalid cycle in workflow sequence");
        } else {
            return true;
        }
        
//        boolean valid = true;
//        valid = valid && (this.numberOfStartSteps() == 1);
//        valid = valid && (this.numberOfFinalSteps() > 0);
//        valid = valid && (this.numberOfActions() > 0);
//        valid = valid && this.checkFinalSteps();
//        valid = valid && this.hasCycle(getStartStep(), new ArrayList<Step>());
//        for (Step step: workflow.getSteps()) {
//            valid = valid && isReachable(getStartStep(), step);
//        }
//        return valid;
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
    return true;
    }
    
    private boolean isReachable (Step actStep,Step stepToReach) {
        if (actStep.getNextSteps().contains(stepToReach)) {
            return true;
        } else {
            for(Step s: actStep.getNextSteps()) {
                return isReachable(s, stepToReach);
            }
            return false;
        }
    }
    
    private boolean checkFinalSteps() {
        for(Step step: workflow.getSteps()) {
            if (step.getNextSteps().isEmpty() && !(step instanceof FinalStep)) {
                return false;
            } else if (step instanceof FinalStep && step.getNextSteps().size() > 0) {
                return false;
            }
        }
        return true;
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