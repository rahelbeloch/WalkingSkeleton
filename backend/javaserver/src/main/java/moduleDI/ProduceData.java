package moduleDI;

import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.User;
import backingbeans.Workflow;
import persistence.Persistence;
import persistence.UserAlreadyExistsException;

public class ProduceData {
	
	private int countWorkflow = 0;
	private int countUser = 0;
	
	
	
	/**
	 * Create a sample Workflow
	 * @param p persistence
	 */
	public void createWorkflow(Persistence p){
		
		Workflow w =new Workflow(countWorkflow);
		w.addStep(new Action(0, "username", "Eins"));
		w.addStep(new Action(0, "username", "Zwei"));
		w.addStep(new Action(0, "username", "Drei"));
		w.addStep(new FinalStep());
		
		p.storeWorkflow(w);
		
		countWorkflow++;
	}
	/**
	 * Create a sample User
	 */
	public void createUser(Persistence p){
		User user = new User();
		user.setId(countUser);
		user.setName(countUser+"");
		try {
			p.addUser(user);
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countUser++;
	}

}
