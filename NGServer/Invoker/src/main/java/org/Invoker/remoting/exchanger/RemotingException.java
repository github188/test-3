package org.Invoker.remoting.exchanger;

public class RemotingException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1936423679890623442L;

	public RemotingException() {
		super();
	}
	
	public RemotingException(String message,Throwable t){
		super(message, t);
	}
	
	public RemotingException(Throwable t){
		super(t);
	}
}
