package org.Invoker.rpc.filter;

import org.Invoker.rpc.SPI;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.result.Result;

@SPI
public interface Filter {

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws InvokerException;
}
