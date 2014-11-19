package processor;

import beans.Action;
import beans.Item;
import beans.Step;
import beans.User;

public class ProcessorManager {
	
	
	public ProcessorManager (){
	
	}
	
	public boolean checkUser(User user, Step step){
		if (step instanceof Action){
			if (user.getId() == ((Action) step).getUserId()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public void selectProcessor(Step step, Item item, User user){
		if(step instanceof Action){
			ActionProcessor actionProcessor = new ActionProcessor();
			actionProcessor.handle(item, step, user);
		}
	}

}
