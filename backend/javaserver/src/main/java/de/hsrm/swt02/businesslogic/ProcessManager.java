package de.hsrm.swt02.businesslogic;

import java.util.Observer;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;

/**
 * Interface for ProcessManager. (Due to Dependency Injection)
 *
 */
public interface ProcessManager extends Observer {

     /**
     * This method checks if the user who wishes to edit a step is the 
     * responsible user who is allowed to execute the step.
     * 
     * @param user who edits the step
     * @param step which user wants to edit
     * @return true if user is "owner" of step and false if not
     */
    boolean checkUser(User user, Step step);

    /**
     * This method selects the processor of a step and executes it.
     * 
     * @param step which is to be edited
     * @param item which is currently active
     * @param user who started interaction
     */
    void selectProcessor(Step step, Item item, User user);
}
