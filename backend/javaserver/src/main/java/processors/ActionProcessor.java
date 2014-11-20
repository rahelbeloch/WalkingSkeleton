package processors;

import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStep;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.User;



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
	public void handle(Item item, AbstractStep step, User user) {
		
		currentItem = item;
		
		currentItem.setStepState(step.getId(), AbstractMetaState.BUSY.toString());
		//funktion irrelevant fuer walking skeleton
		currentItem.setStepState(step.getId(), AbstractMetaState.DONE.toString());
		for(AbstractStep s : step.getNextSteps()){
			if(!(s instanceof FinalStep)){ 
				//TODO aktuelles item persiztieren
				currentItem.setStepState(s.getId(), AbstractMetaState.OPEN.toString());
			}else{
				currentItem.setStepState(s.getId(), AbstractMetaState.DONE.toString());
				currentItem.setFinished(true);
			}	
		}
	}
}
