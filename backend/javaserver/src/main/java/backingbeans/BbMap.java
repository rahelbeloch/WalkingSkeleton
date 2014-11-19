package backingbeans;

import java.util.HashMap;

import beans.Map;
import beans.MetaState;

/**
 * This class is the backingbean of the map bean. 
 * @author jvanh001
 *
 */
public class BbMap extends Map{
	
	private HashMap<Integer, MetaState> map;
	
	/**
	 * The constructor initializes an java.util.HashMap build 
	 */
	public BbMap(){
		map = new HashMap<Integer, MetaState>();
		
		for(int i : key){
			map.put(i, value.get(i));
		}
	}
	
	public HashMap<Integer, MetaState> getMap(){
		return map;
	}
	
	public void setValue(int stepId, MetaState state){
		map.put(stepId, state);
	}


}
