package backingbeans;

import java.util.HashMap;

import abstractbeans.AbstractMap;
import abstractbeans.AbstractMetaState;

/**
 * This class is the backingbean of the map bean. 
 * @author jvanh001
 *
 */
public class Map extends AbstractMap{
	
	private HashMap<Integer, AbstractMetaState> map;
	
	/**
	 * The constructor initializes an java.util.HashMap build 
	 */
	public Map(){
		map = new HashMap<Integer, AbstractMetaState>();
		
	}
	
	public HashMap<Integer, AbstractMetaState> getMap(){
		return map;
	}
	
	
	public void setValue(int stepId, AbstractMetaState state){
		map.put(stepId, state);
	}


}
