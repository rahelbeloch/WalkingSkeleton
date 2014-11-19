package processors;

import backingbeans.Workflow;

/**
 * This class initializes the execution of a workflow.
 * @author jvanh001
 *
 */
public class StartTrigger {
	
	private Workflow currentWorkflow;
	private StartProcessor startProcessor;
	
	
	/**
	 * Constructor of StartTrigger
	 * @param workflow which was initialized by an admin
	 */
	public StartTrigger(Workflow workflow){
		currentWorkflow = workflow;
		startProcessor = new StartProcessor();
	}
	
	
	/**
	 * This method creates an item for its workflow.
	 */
	public void startWorkflow(){
		startProcessor.createItem(currentWorkflow);
	}
}
