package de.hsrm.swt02.businesslogic.processors;

import java.util.Observer;

import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;

import com.google.inject.Inject;

/**
 * This class initializes the execution of a workflow.
 *
 */
public class StartTrigger {

    private Workflow currentWorkflow;
    private StartProcessor startProcessor;

    /**
     * Constructor of StartTrigger.
     * 
     * @param workflow which was initialized by an admin
     * @param o is the observer of starttrigger
     * @param p is a singleton instace of the persistence
     */
    @Inject
    public StartTrigger(Workflow workflow, Observer o, Persistence p) {
        currentWorkflow = workflow;
        startProcessor = new StartProcessor(p);
        startProcessor.addObserver(o);
    }

    /**
     * This method creates an item for its workflow.
     */
    public void startWorkflow() {
        startProcessor.createItem(currentWorkflow);
    }
}
