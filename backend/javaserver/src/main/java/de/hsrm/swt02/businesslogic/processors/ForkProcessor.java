package de.hsrm.swt02.businesslogic.processors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Level;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Fork;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This class executes "Fork" step-objects.
 *
 */
public class ForkProcessor implements StepProcessor {

    private Persistence p;
    public static final UseLogger LOGGER = new UseLogger();
    
    final private ScriptEngineManager sm = new ScriptEngineManager();
    final private ScriptEngine sEngine = sm.getEngineByName("jython");
    
    /**
     * Constructor of ForkProcessor.
     * 
     * @param p is a singleton instance of the persistence
     */
    @Inject
    public ForkProcessor(Persistence p) {
        this.p = p;
    }

    /**
     * This method is initiated by an User. The responsible User sends the item
     * and the step, which they wish to edit. The sent item will be modified and
     * saved. The current step's state of the item will be set on "BUSY" only if
     * it was setted to "OPEN".
     * 
     * @param item which is currently edited
     * @param step which is currently executed
     * @param user who currently executes the step
     * @throws LogicException if there are problems while working on an item
     * @return itemId of item which was edited
     */
    public String handle(Item item, Step step, User user) throws LogicException {
        final Workflow workflow = p.loadWorkflow(item.getWorkflowId());      
        final Item currentItem = workflow.getItemById(item.getId());
        final String stepId = step.getId();
        final Step currentStep = workflow.getStepById(stepId);
        final String itemId = currentItem.getId();
        
        boolean result = true;
        
        if(currentStep instanceof Fork) {
        	Fork fork = (Fork) currentStep;
        	final String script = fork.getScript();
        	
	        try {
	            sEngine.eval(script);
	            
	            // todo: simons algorithmus
	            
	            Invocable inv = (Invocable) sEngine;
	            Object o = inv.invokeFunction("check", new HashMap<String, String>());
	            if (o instanceof Boolean) {
	            	o = (Boolean)o;
	            } 
	        } catch (ScriptException e) {
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        }
	        
	        currentItem.setStepState(stepId, MetaState.DONE.toString());
	        
	        Step nextStep = result ? fork.getTrueBranch() : fork.getFalseBranch();
	        
	        if (!(nextStep instanceof FinalStep)) {
                currentItem.setStepState(nextStep.getId(), MetaState.OPEN.toString());
            } else {
                currentItem.setStepState(nextStep.getId(), MetaState.DONE.toString());
                currentItem.setFinished(true);
                LOGGER.log(Level.INFO, "[logic] Successfully finished item " + itemId + " from workflow " + currentItem.getWorkflowId());
            }
	        
	        try {
	            p.storeWorkflow(workflow);
	        } catch (WorkflowNotExistentException e) {
	            e.printStackTrace();
	        }
	        
	        if (nextStep instanceof Fork) {
	        	handle(currentItem, nextStep, user);
	        }
        }

        return itemId;
    }
}
