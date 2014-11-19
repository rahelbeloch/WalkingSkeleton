package backingbeans;

import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaState;

/**
 * This is the backingbean of the item bean. 
 * @author jvanh001
 *
 */
public class Item extends AbstractItem{
	
	public Item (){
		metadata = new Map();
	}
	
	/**
	 * Get BbMap (HashMap) of metadata
	 * @return
	 */
	public Map getMetaData(){
		return (Map) metadata;
	}
	
	public void add(int stepId, AbstractMetaState state){
		((Map) metadata).getMap().put(stepId, state);
	}
	
	public void setMetaState(int stepId, AbstractMetaState state){
		((Map) metadata).setValue(stepId, state);
	}
	
	public Map getMetadata() {
		return (Map)metadata; // Casting an dieser Stelle nötig?
	}
	
}
