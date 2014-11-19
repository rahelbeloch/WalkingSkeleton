package processors;

import abstractbeans.AbstractStep;
import backingbeans.Item;
import backingbeans.User;


/**
 * This is the interface of the StepProcessor.
 * @author jvanh001
 */
public interface StepProcessor {
	
	/**
	 * This is the method header for executing a step
	 * @param item which is active in a workflow
	 * @param step which an user wishes to edit
	 * @param user who sent an edit request
	 */
	void handle (Item item, AbstractStep step, User user);

}
