package backingbeans;

import beans.Item;

import beans.MetaState;
import beans.Step;

public class BbItem extends Item{
	
	public BbItem (){
		metadata = new BbMap();
	}
	
	public void setMetaState(Step step, MetaState state){
		((BbMap) metadata).setValue(step.getId(), state);
	}
	

}
