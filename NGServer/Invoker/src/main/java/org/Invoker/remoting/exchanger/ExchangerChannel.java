package org.Invoker.remoting.exchanger;

public interface ExchangerChannel extends Channel{

	public ResponseFuture request(Object request) throws RemotingException;

	public ResponseFuture request(Object request, int timeout) throws RemotingException;

	public ExchangerHandler getExchangeHandler() throws RemotingException;

}
