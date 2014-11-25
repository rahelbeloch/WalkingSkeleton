package backingbeans;

import abstractbeans.AbstractAction;

public class Action extends AbstractAction{
	
	public Action(int id, int userId, String name){
		super.name = name;
		super.userId = userId; // shouldn't that be userName? DN
		super.setId(id);
	}

}
