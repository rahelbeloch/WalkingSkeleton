package de.hsrm.swt02.businesslogic;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.processors.StepProcessor;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;

/**
 * Interface for ProcessManager. (Due to Dependency Injection)
 *
 */
public interface ProcessManager {

    /**
     * This method checks if the inquired user can actually start the workflow.
     * If she/he can a workflow is started.
     * 
     * @param workflow which will be started
     * @param username indicates who wants to start a workflow
     * @throws LogicException 
     * @return String itemId of the item created within starting the wf
     */
    String startWorkflow(Workflow workflow, String username) throws LogicException;

    /**
     * This method selects the appropriate stepprocessor for a step.
     * 
     * @param step which will be executed
     * @return StepProcessor fitting processor for this step
     */
    StepProcessor selectProcessor(Step step);

    /**
     * This method executes the step operation.
     * 
     * @param step which is to be edited
     * @param item which is currently active
     * @param user who started interaction
     * @throws LogicException 
     * @return itemId of the item which step was executed
     */
    String executeStep(Step step, Item item, User user) throws LogicException;
}