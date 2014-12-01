package de.hsrm.swt02.persistence.exceptions;

public class WorkflowNotExistentException extends NotExistentException {

    private static final long serialVersionUID = 3157473446452493460L;
    private int errorcode = 11252;

    public WorkflowNotExistentException() {
        super();
    }

    public WorkflowNotExistentException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
}
