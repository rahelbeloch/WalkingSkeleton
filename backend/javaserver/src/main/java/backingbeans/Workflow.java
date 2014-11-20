package backingbeans;

import abstractbeans.AbstractItem;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractWorkflow;

public class Workflow extends AbstractWorkflow {

	
	public Workflow (int workflowId){
		id = workflowId;
	}
	
	/**
	 * This method returns a step object by its position in the step list of a workflow.
	 * @param pos states the index of the searched step object
	 * @return searched step object if found
	 */
	public AbstractStep getStepByPos(int pos){
		return getStep().get(pos);
	}
	
	
	/**
	 * This method returns a step object by its id.
	 * @param stepId states which certain object is looked for
	 * @return searched step object if found, else return null
	 */
	public AbstractStep getStepById(int stepId){
		for(AbstractStep as : getStep()){
			if(as.getId() == stepId){
				return as;
			}
		}
		return null;
	}
	
	
	/**
	 * This method adds single steps to a workflow.
	 * @param step which will be added to the step list of a workflow
	 */
	public void addStep(AbstractStep step){
		getStep().add(step);
	}
	
	
	/**
	 * This method removes a certain step by its id.
	 * @param stepId states which step should be removed
	 */
	public void removeStep(int stepId){
		for(AbstractStep as : getStep()){
			if(as.getId() == stepId){
				getStep().remove(as);
			}
		}
	}
	
	
	/**
	 * This method adds a new item of a workflow.
	 * @param item which will be added to the item lsit of a workflow
	 */
	public void addItem(AbstractItem item){
		getItem().add(item);
	}
	
	
	/**
	 * This method removes a certain item of a workflow.
	 * @param itemId states which item should be removed
	 */
	public void removeItem(int itemId){
		for(AbstractItem ai : getItem()){
			if(ai.getId() == itemId){
				getItem().remove(ai);
			}
		}
	}
	
	
	/**
	 * This method connects steps with its straight neighbor. The last step has no neighbor.
	 */
	public void connectSteps(){
		for(int i = 0; i < getStep().size() - 1; i++){
			getStep().get(i).getNextSteps().add(getStep().get(i+1));
		}
	}
}
