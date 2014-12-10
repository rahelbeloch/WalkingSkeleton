package de.hsrm.swt02.businesslogic.exceptions;
    
    /**
     * Exception dealing with whenever something is requested and it's alread there.
     */
    public class NoPermissionException extends LogicException {
        
        /**
         * 
         */
        private static final long serialVersionUID = -8295181776614315758L;
        public static final int ERRORCODE = 11300;
        
        /**
         * Constructor for the Exception.
         */
        public NoPermissionException() {
            super("no permission for operation.");
        }

        /**
         * Method dealing with the error message.
         * @param msg is the errormessage.
         */
        public NoPermissionException(String msg) {
            super(msg);
        }
        
        /**
         * Getter for the errorcode.
         * @return ERRORCODE is the errorcode
         */
        public int getErrorCode() {
            return NoPermissionException.ERRORCODE;
        }


}
