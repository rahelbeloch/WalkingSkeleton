package processor;

import beans.FinalStep;
import beans.Item;
import beans.MetaState;
import beans.Step;
import beans.User;

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
	public void handle(Item item, Step step, User user) {
		currentItem = item;
		
		item.getMetadata().getValue().set(item.getMetadata().getKey().indexOf(step.getId()), MetaState.BUSY);
		//funktion irrelevant für walking skeleton
		item.getMetadata().getValue().set(item.getMetadata().getKey().indexOf(step.getId()), MetaState.DONE);
		
		for(Step s : step.getNextSteps()){
			if(!(s instanceof FinalStep)){ 
				//TODO aktuelles item persiztieren
				item.getMetadata().getValue().set(item.getMetadata().getKey().indexOf(s.getId()), MetaState.OPEN);
				
			}else{
				//TODO setze finish-flag in item
				//zweite loesung
				item.getMetadata().getValue().set(item.getMetadata().getKey().indexOf(s.getId()), MetaState.DONE);
			}	
		}
		
	}




}
