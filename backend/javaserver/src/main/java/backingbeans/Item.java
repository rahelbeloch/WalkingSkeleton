package backingbeans;

import java.util.ArrayList;
import java.util.List;
import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractMetaState;

/**
 * This is the backingbean of the item bean. 
 * @author jvanh001
 *
 */
public class Item extends AbstractItem{
	
	private String state;
	
	/**
	 * Default-Constructor
	 */
	public Item (){
		metadata = getMetadata();
	}
	
	
	/**
	 * This method gets an entry of the metadata list which suits the group and key parameters.
	 * @param key is the id of an entry
	 * @param group is the type of an entry 
	 * @return the suitable entry, else return NULL
	 */
	public AbstractMetaEntry getEntry(String key, String group) {
		
		for(AbstractMetaEntry a: metadata){
			if(a.getGroupId().equals(group) && a.getKey().equals(key)){
				return a;
			}
		}
		return null;
	}
	
	
	/**
	 * This method gets the value of an entry.
	 * @param key is the id of an entry
	 * @param group is the type of an entry
	 * @return the suitable entry value, if the entry was not found return NULL
	 */
	public String getEntryValue(String key, String group){
		
		AbstractMetaEntry ame = getEntry(key, group);
		
		if (ame != null) {
			return ame.getValue();
		}
		return null;
	}
	
	
	/**
	 * This method returns the Metastate of an entry.
	 * @param key is the id of an entry
	 * @return the Metastate of the searched entry
	 */
	public String getStepState(int key) {
		
		return AbstractMetaState.fromValue(getEntryValue(key + "", "step")).toString();
	}
	
	
	/**
	 * This method gets a list which contains looked for entries.
	 * @param group is the type of the entries which are looked for
	 * @return a list of suitable entries
	 */
	public List<AbstractMetaEntry> getForGroup(String group){
		
		List<AbstractMetaEntry> list = new ArrayList<AbstractMetaEntry>();
	
		for(AbstractMetaEntry a: metadata){
			if(a.getGroupId().equals(group)){
				list.add(a);
			}
		}
		return list;
	}
	
	
	/**
	 * This method sets the value of an entry or adds a new entry.
	 * @param key is the id of an entry
	 * @param group is the type of an entry
	 * @param value represents an entrie's content
	 */
	public void set(String key, String group, String value){
		
		AbstractMetaEntry ame  = getEntry(key, group);
		
		if (ame != null) {
			ame.setValue(value);
		} else {
			AbstractMetaEntry entry = new AbstractMetaEntry();
			entry.setGroupId(group);
			entry.setKey(key);
			entry.setValue(value);
			metadata.add(entry);
		}
	}
	
	
	/**
	 * This method sets specifically the metastate of an step entry.
	 * @param key is the id of an entry
	 * @param value is the new state of an entry
	 */
	public void setStepState(int key, String value) {
		
		set(Integer.toString(key), "step", value);
	}
	
	
	/**
	 * This methods is just for the initial state setting of an step.
	 * @param value has to be OPEN, for enabling very first Step
	 */
	public void setFirstStepState(String value){
		
		getForGroup("step").get(0).setValue(value);
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
}


