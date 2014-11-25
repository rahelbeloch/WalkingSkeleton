package manager;

import java.util.Observable;
import java.util.Observer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import messaging.ServerPublisher;
import backingbeans.Action;
import backingbeans.Item;
import backingbeans.User;
import processors.ActionProcessor;
import abstractbeans.AbstractAction;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;

/**
 * This class handles the processing of Steps. (For now) it provides methods for checking whether an user can execute a step and selecting
 * the right processor for a step.  
 *
 */
@Singleton
public class ProcessManagerImp implements Observer, ProcessManager{
	
	private ServerPublisher sp;
	
	/**
	 * Constructor of ProcessManager
	 */
	@Inject
	public ProcessManagerImp (ServerPublisher sp){
		this.sp = sp;
		sp.startBroker();
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
	public void selectProcessor(AbstractStep step, Item item, User user){
		
		if(step instanceof Action){
			ActionProcessor actionProcessor = new ActionProcessor(this);
			actionProcessor.handle(item, step, user);
		}
	}

	/**
	 * This method is executed if its observables notifies changes.
	 */
	public void update(Observable o, Object arg) {
		try {
			//TODO extracting necessary information of object arg (for now <Datenstruktur>, <Operation>, <Id>
			sp.publish("<Datenstruktur>=<Operation>=<Id>", "WORKFLOW_INFO");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
