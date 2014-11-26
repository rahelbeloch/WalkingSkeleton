package processors;

import java.util.Observable;

import persistence.Persistence;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStep;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.User;

import com.google.inject.Inject;



/**
 * This class executes "Action" step-objects.
 *
 */
public class ActionProcessor extends Observable implements StepProcessor {
	
	private Item currentItem;
	private Persistence p;
	
	/**
	 * Constructor of ActionProcessor
	 * @param pm is the observer and manager of the processor
	 */
	@Inject
	public ActionProcessor (Persistence p){
		this.p = p;
	}
	
	/**
	 * This method is initiated by an User. The responsible User sends the item and the step, which they wish to edit.
	 * The sent item will be modified and saved. First the current step's state of the item will be set on "BUSY". 
	 * After successfully executing the Action-function the current step's state will be set on "DONE".
	 * If the next step isn't an end state, this processor sets the state of the current step's straight neighbor to OPEN. 
	 */
	public void handle(Item item, AbstractStep step, User user) {
		
		currentItem = item;
		currentItem.setStepState(step.getId(), AbstractMetaState.BUSY.toString());
		currentItem.setState("upd");
		p.storeItem(currentItem);
		setChanged();
		notifyObservers(currentItem);
		
		//funktion irrelevant fuer walking skeleton
		currentItem.setStepState(step.getId(), AbstractMetaState.DONE.toString());
		currentItem.setState("upd");
		p.storeItem(currentItem);
		setChanged();
		notifyObservers(currentItem);
		
		for(AbstractStep s : step.getNextSteps()){
			if(!(s instanceof FinalStep)){ 
				currentItem.setStepState(s.getId(), AbstractMetaState.OPEN.toString());
				currentItem.setState("upd");
				p.storeItem(currentItem);
				setChanged();
				notifyObservers(currentItem);
			}else{
				currentItem.setStepState(s.getId(), AbstractMetaState.DONE.toString());
				currentItem.setFinished(true);
				currentItem.setState("upd");
				p.storeItem(currentItem);
				setChanged();
				notifyObservers(currentItem);
			}	
		}
	}
}
