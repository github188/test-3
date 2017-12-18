package org.Invoker.rpc.invoker;

import java.io.Serializable;

import org.Invoker.rpc.exception.InvokerException;

public interface InvokerNotify extends Serializable{

	public void notify(Object result, Throwable e) throws InvokerException;
}
