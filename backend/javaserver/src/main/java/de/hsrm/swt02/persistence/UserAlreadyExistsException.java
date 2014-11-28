package de.hsrm.swt02.persistence;

public class UserAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 4328135084952400723L;

    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
