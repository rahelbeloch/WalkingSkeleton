package processor;

import de.hsrm.mi.gruppe02.javaserver.beans.Item;
import de.hsrm.mi.gruppe02.javaserver.beans.MetaState;
import de.hsrm.mi.gruppe02.javaserver.beans.Step;
import de.hsrm.mi.gruppe02.javaserver.beans.User;

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
		
		item.getMetadata().getValue(step.getId()).setValue(MetaState.BUSY);//TODO: Jerome will noch eine methode schreiben die das passende objekt zurueck gibt
		//funktion irrelevant für walking skeleton
		item.getMetadata().getValue(step.getId()).setValue(MetaState.DONE);
		if(next != ende){ //TODO: waerst du so nett, EZ? xD Man muss noch den Next-aufruf schreiben sowie den "Endzustand" abfangen
			//rufe handle-methode von nachfolgenden step auf
		
		}else{
			//setze finish-flag in workflow
		}
	}




}
