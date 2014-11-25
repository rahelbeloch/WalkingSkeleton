package processors;

import com.google.inject.Inject;

import manager.ProcessManager;
import backingbeans.Workflow;

/**
 * This class initializes the execution of a workflow.
 *
 */
public class StartTrigger {
	
	private Workflow currentWorkflow;
	private StartProcessor startProcessor;
	
	
	/**
	 * Constructor of StartTrigger
	 * @param workflow which was initialized by an admin
	 * @param pm is the observer and manager of the processor
	 */
	@Inject
	public StartTrigger(Workflow workflow, ProcessManager pm){
		currentWorkflow = workflow;
		startProcessor = new StartProcessor(pm);
	}
	
	/**
	 * This method creates an item for its workflow.
	 */
	public void startWorkflow(){
		startProcessor.createItem(currentWorkflow);
	}
}
