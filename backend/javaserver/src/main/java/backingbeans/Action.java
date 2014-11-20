package backingbeans;

import abstractbeans.AbstractAction;

public class Action extends AbstractAction{
	
	public Action(int id, int userId, String name){
		super.name = name;
		super.userId = userId;
		super.setId(id);
	}

}
