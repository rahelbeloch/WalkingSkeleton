package moduledi;

import java.util.LinkedList;
import java.util.List;

import manager.ProcessManager;
import messaging.ServerPublisher;
import persistence.Persistence;
import persistence.UserAlreadyExistsException;
import processors.StartTrigger;
import abstractbeans.AbstractItem;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractWorkflow;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class LogicImp implements Logic{
	
	private ServerPublisher sp;
	private Persistence p;
	private ProcessManager pm;
	
	public LogicImp() {
		Injector i = Guice.createInjector(new SingleModule());
		sp = i.getInstance(ServerPublisher.class);
		p = i.getInstance(Persistence.class);
		pm = i.getInstance(ProcessManager.class);
	}
	
	@Override
	public void startWorkflow(int workflowID, User user) {
		// TODO: check user permission
		Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
		StartTrigger start = new StartTrigger(workflow, pm, p);
		start.startWorkflow();
	}

	@Override
	public void addWorkflow(Workflow workflow) {
		p.storeWorkflow(workflow);	
	}

	@Override
	public Workflow getWorkflow(int workflowID) {
		return (Workflow) p.loadWorkflow(workflowID);
	}

	@Override
	public void deleteWorkflow(int workflowID) {
		p.deleteWorkflow(workflowID);
	}

	@Override
	public void stepOver(Item item, AbstractStep step, User user) {
		pm.selectProcessor(step, item, user);
	}

	@Override
	public void addStep(int workflowID, AbstractStep step) {
		Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
		workflow.addStep(step);
		p.storeWorkflow(workflow);
	}

	@Override
	public void deleteStep(int workflowID, int stepID) {
		Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
		workflow.removeStep(stepID);
		p.storeWorkflow(workflow);
	}

	@Override
	public void addUser(User user) throws UserAlreadyExistsException {
		p.addUser(user);
	}

	@Override
	public User getUser(String username) {
		return (User) p.loadUser(username);
	}

	@Override
	public void deleteUser(String username) {
		//System.out.println("Do you really really really want to delete a user?");
		p.deleteUser(username);		
	}

	@Override
	public List<Workflow> getWorkflowsByUser(User user) {
		LinkedList<Workflow> workflows = new LinkedList<>();
		for(AbstractWorkflow wf: p.loadAllWorkflows()) {
			for(AbstractStep step: wf.getStep()) {
				//TODO username in AbstractStep
				if(step.getUsername().equals(user.getName())) {
					workflows.add((Workflow)wf);
					break;
				}
			}
		}
		return workflows;
	}

	@Override
	public List<Item> getOpenItemsByUser(User user) {
		LinkedList<Workflow> workflows = (LinkedList)getWorkflowsByUser(user);
		for(Workflow wf: workflows) {
			for(AbstractItem item: wf.getItem()) {
				
			}
		}
	}

	@Override
	public List<Workflow> getStartableWorkflows(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
