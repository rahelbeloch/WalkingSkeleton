package backingbeans;

import abstractbeans.AbstractAction;

public class Action extends AbstractAction{
	
	public Action(int id, String username, String name){
		super.name = name;

		super.username = username; 

		super.setId(id);
	}

}
