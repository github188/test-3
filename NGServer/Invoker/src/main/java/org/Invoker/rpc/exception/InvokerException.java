package org.Invoker.rpc.exception;

public class InvokerException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8526225846349701792L;

	public InvokerException() {
        super();
    }

    public InvokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokerException(String message) {
        super(message);
    }

    public InvokerException(Throwable cause) {
        super(cause);
    }
    
}
