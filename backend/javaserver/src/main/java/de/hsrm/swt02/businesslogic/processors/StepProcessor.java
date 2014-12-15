package de.hsrm.swt02.businesslogic.processors;

import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

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
     * @exception ItemNotForwardableException if the responding steplist of the given item is not any more forwardable
     * @exception UserHasNoPermissionException if the given user has no permission to operate on the item
     * @throws ItemNotForwardableException
     * @throws UserHasNoPermissionException
     */
    void handle(Item item, Step step, User user) throws ItemNotForwardableException, UserHasNoPermissionException, ItemNotExistentException, StorageFailedException;
}
