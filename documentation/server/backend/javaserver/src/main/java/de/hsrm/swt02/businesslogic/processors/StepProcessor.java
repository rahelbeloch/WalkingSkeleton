package de.hsrm.swt02.businesslogic.processors;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;

/**
 * This is the interface of the StepProcessor.
 * 
 */
public interface StepProcessor {

    /**
     * This is the method header for executing a step.
     * 
     * @param item which is active in a workflow
     * @param step which an user wishes to edit
     * @param user who sent an edit request
     * @throws LogicException if there are problems while working on an item
     * @return String is itemid which was edited
     */
    String handle(Item item, Step step, User user) throws LogicException;
}
