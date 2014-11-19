package backingbeans;

import beans.Item;

import beans.MetaState;

public class BbItem extends Item{
	
	public BbItem (){
		metadata = new BbMap();
	}
	
	
	public BbMap getMetaData(){
		return (BbMap) metadata;
	}
	
	public void add(int stepId, MetaState state){
		((BbMap) metadata).getMap().put(stepId, state);
	}
	
	public void setMetaState(int stepId, MetaState state){
		((BbMap) metadata).setValue(stepId, state);
	}
	
	
	

}
