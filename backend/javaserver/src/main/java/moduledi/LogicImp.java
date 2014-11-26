package moduledi;

import java.util.LinkedList;
import java.util.List;

import manager.ProcessManager;
import messaging.ServerPublisher;
import model.Item;
import model.Step;
import model.User;
import model.Workflow;
import persistence.Persistence;
import persistence.UserAlreadyExistsException;
import processors.StartTrigger;

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
	public void stepOver(Item item, Step step, User user) {
		pm.selectProcessor(step, item, user);
	}

	@Override
	public void addStep(int workflowID, Step step) {
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

	// TODO: Ez at Home
	@Override
	public List<Workflow> getWorkflowsByUser(User user) {
		LinkedList<Workflow> workflows = new LinkedList<>();
		for(Workflow wf: p.loadAllWorkflows()) {
			for(Step step: wf.getSteps()) {
				//TODO username in AbstractStep
				if(step.getUsername().equals(user.getUsername())) {
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
			for(Item item: wf.getItems()) {
				
			}
		}
		return null;
	}

	@Override
	public List<Workflow> getStartableWorkflows(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
