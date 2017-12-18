package net.transfer.client;

public interface ExchangerChannel extends Channel{

	public ResponseFuture request(byte servId,Object request) throws RemotingException;

	public ResponseFuture request(byte servId,Object request, int timeout) throws RemotingException;

	public ExchangerHandler getExchangeHandler() throws RemotingException;

}
