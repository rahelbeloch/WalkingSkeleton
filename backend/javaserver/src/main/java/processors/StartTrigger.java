package processors;

import abstractbeans.AbstractWorkflow;

/**
 * This class initializes the execution of a workflow.
 * @author jvanh001
 *
 */
public class StartTrigger {
	
	private AbstractWorkflow currentWorkflow;
	private StartProcessor startProcessor;
	
	/**
	 * Constructor of StartTrigger
	 * @param workflow which was initialized by an admin
	 */
	public StartTrigger(AbstractWorkflow workflow){
		currentWorkflow = workflow;
		startProcessor = new StartProcessor();
	}
	
	/**
	 * This method creates an Item for its workflow.
	 */
	public void startWorkflow(){
		startProcessor.createItem(currentWorkflow);
	}

}
