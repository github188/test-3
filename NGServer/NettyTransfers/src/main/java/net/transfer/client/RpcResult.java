package net.transfer.client;

import java.io.Serializable;

public class RpcResult implements Result, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 426717762262941323L;
	
	private Throwable exception = null;
	
	private Object value = null;
	
	public RpcResult(){
		
	}
	
	public RpcResult(Throwable exception){
		this.exception = exception;
	}
	
	public RpcResult(Object value){
		this.value = value;
	}

	public Throwable getException() {
		return exception;
	}

	public Object getValue() {
		return value;
	}

	public boolean hasException() {
		return exception==null?false:true;
	}

	public Object recreate() throws Throwable {
		if(exception!=null){
			throw exception;
		}
		return value;
	}

}
