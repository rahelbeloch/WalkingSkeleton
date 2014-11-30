package de.hsrm.swt02.persistence;

public class WorkflowNotExistentException extends Exception {

    private static final long serialVersionUID = 3157473446452493460L;

    public WorkflowNotExistentException() {
        super();
    }

    public WorkflowNotExistentException(String msg) {
        super(msg);
    }
}
