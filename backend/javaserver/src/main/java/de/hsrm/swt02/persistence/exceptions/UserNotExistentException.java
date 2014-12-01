package de.hsrm.swt02.persistence.exceptions;

public class UserNotExistentException extends Exception {

    private static final long serialVersionUID = 4657473446452493460L;
    int errorcode = 11251;
    public UserNotExistentException() {
        super();
    }

    public UserNotExistentException(String msg) {
        super(msg);
    }
}
