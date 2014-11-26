package processors;

import model.Item;
import model.Step;
import model.User;


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
	void handle (Item item, Step step, User user);

}
