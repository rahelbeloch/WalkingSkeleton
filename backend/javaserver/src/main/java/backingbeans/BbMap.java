package backingbeans;

import java.util.HashMap;

import beans.Map;
import beans.MetaState;

public class BbMap extends Map{
	
	private HashMap<Integer, MetaState> map;
	
	public BbMap(){
		map = new HashMap<Integer, MetaState>();
		
		for(int i : key){
			map.put(i, value.get(i));
		}
	}
	
	public void setValue(int stepId, MetaState state){
		map.put(stepId, state);
	}
	
}
