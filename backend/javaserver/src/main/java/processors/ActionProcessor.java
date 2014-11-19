package processors;

import abstractbeans.AbstractFinalStep;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStep;
import backingbeans.Item;


/**
 * This class executes "Action" step-objects.
 * @author jvanh001
 *
 */
public class ActionProcessor implements StepProcessor {
	private Item currentItem;

	/**
	 * Default-Constructor
	 */
	public ActionProcessor (){
		
	}
	
	/**
	 * This method is initiated by an User. The responsible User sends the item and the step, which they wish to edit.
	 * The sent item will be modified and saved. First the current step's state of the item will be set on "BUSY". 
	 * After successfully executing the Action-function the current step's state will be set on "DONE".
	 * If the next step isn't an end state, this processor calls the next step's processor etc. until it reaches an end state.
	 */
	public void handle(Item item, AbstractStep step) {
		currentItem = (Item)item;
		
		currentItem.setMetaState(step.getId(), AbstractMetaState.BUSY);
		//funktion irrelevant für walking skeleton
		currentItem.setMetaState(step.getId(), AbstractMetaState.DONE);
		
		for(AbstractStep s : step.getNextSteps()){
			if(!(s instanceof AbstractFinalStep)){ 
				//TODO aktuelles item persiztieren
				currentItem.setMetaState(s.getId(), AbstractMetaState.OPEN);
				
			}else{
				//TODO setze finish-flag in item
				//zweite loesung
				currentItem.setMetaState(s.getId(), AbstractMetaState.DONE);
				currentItem.setFinished(true);
			}	
		}
		
	}





}
