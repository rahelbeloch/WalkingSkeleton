package processor;

import beans.Action;
import beans.Item;
import beans.MetaState;
import beans.Step;
import beans.Workflow;
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
	 * @param workflow
	 */
	public void createItem(Workflow workflow){
		currentItem = new Item();
		//irgendetwas passiert hier noch
		initiateItem(workflow, currentItem);
	}
	
	/**
	 * This method instantiates an item's Metadata. The default MetaState is INACTIVE. 
	 * Only "Action" step-objects are considered. The state of the very first step is set to "OPEN".
	 * @param workflow
	 * @param item
	 */
	public void initiateItem(Workflow workflow, Item item){
		for (Step s: workflow.getStep()){
			if (s instanceof Action){ 
				currentItem.getMetadata().getKey().add(s.getId());
				currentItem.getMetadata().getValue().add(MetaState.INACTIVE);
			}
		}
		currentItem.getMetadata().getValue().set(0,MetaState.OPEN); 
		//TODO erstelltes item in der persistenz abspeichern
	}



}
