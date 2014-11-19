package processors;

import backingbeans.BbItem;
import beans.FinalStep;
import beans.Item;
import beans.MetaState;
import beans.Step;

/**
 * This class executes "Action" step-objects.
 * @author jvanh001
 *
 */
public class ActionProcessor implements StepProcessor {
	private BbItem currentItem;

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
	public void handle(Item item, Step step) {
		currentItem = (BbItem)item;
		
		currentItem.setMetaState(step.getId(), MetaState.BUSY);
		//funktion irrelevant für walking skeleton
		currentItem.setMetaState(step.getId(), MetaState.DONE);
		
		for(Step s : step.getNextSteps()){
			if(!(s instanceof FinalStep)){ 
				//TODO aktuelles item persiztieren
				currentItem.setMetaState(s.getId(), MetaState.OPEN);
				
			}else{
				//TODO setze finish-flag in item
				//zweite loesung
				currentItem.setMetaState(s.getId(), MetaState.DONE);
				currentItem.setFinished(true);
			}	
		}
		
	}




}
