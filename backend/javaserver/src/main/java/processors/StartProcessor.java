package processors;

import abstractbeans.AbstractAction;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStep;
import backingbeans.Item;
import backingbeans.Workflow;


/**
 * 
 * @author jvanh001
 * This class implements the function of StartTrigger. It creates a new item and saves it.
 */
public class StartProcessor {

	private Item currentItem;
	
	/**
	 * Default-Constructor
	 */
	public StartProcessor(){
	}
	
	
	/**
	 * This method receives an workflow-object and creates an item and calls a method which instantiates its Metadata
	 * according to the workflow's initialized steps. The item consists of pairs of step IDs and its MetaState. 
	 * @param workflow it's id will be noted within a new item
	 */
	public void createItem(Workflow workflow){
		
		currentItem = new Item();
		currentItem.setId(workflow.getId()*1000 + workflow.getStep().size());
		currentItem.setWorkflowId(workflow.getId());
		initiateItem(workflow, currentItem);
	}
	
	
	/**
	 * This method instantiates an item's Metadata. The default MetaState is INACTIVE. 
	 * Only "Action" step-objects are considered. The state of the very first step is set to "OPEN".
	 * @param workflow provides a step list, which will be transformed into Metadatas
	 * @param item is the freshly created item for a workflow
	 */
	public void initiateItem(Workflow workflow, Item item){
		
		for (AbstractStep s: workflow.getStep()){
			if (s instanceof AbstractAction){ 
				item.set(s.getId()+"",  "step", AbstractMetaState.INACTIVE.toString());
			}
		}
		item.setStepState(0, AbstractMetaState.OPEN);
		//TODO erstelltes item in der persistenz abspeichern
	}
}
