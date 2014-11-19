package manager;

import backingbeans.Item;
import processors.ActionProcessor;
import abstractbeans.AbstractAction;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;

/**
 * This class handles the processing of Steps. (For now) it provides methods for checking whether an user can execute a step and selecting
 * the right processor for a step.  
 * @author jvanh001
 *
 */
public class ProcessManager {
	
	/**
	 * Default-Constructor
	 */
	public ProcessManager (){
	}
	
	/**
	 * This method checks if the user who wishes to edit a step is the responsible user who is allowed to execute the step.
	 * @param user who edits the step
	 * @param step which user wants to edit
	 * @return true if user is "owner" of step and false if not
	 */
	public boolean checkUser(AbstractUser user, AbstractStep step){
		if (step instanceof AbstractAction){
			if (user.getId() == ((AbstractAction) step).getUserId()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * This method selects the processor of a step and executes it.
	 * @param step which is to be edited
	 * @param item which is currently active
	 * @param user who started interaction
	 */
	public void selectProcessor(AbstractStep step, Item item, AbstractUser user){
		if(step instanceof AbstractAction){
			ActionProcessor actionProcessor = new ActionProcessor();
			actionProcessor.handle(item, step);
		}
	}

}
