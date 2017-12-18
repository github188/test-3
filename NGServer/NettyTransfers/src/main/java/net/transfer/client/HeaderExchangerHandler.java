package net.transfer.client;

public class HeaderExchangerHandler implements ChannelHandler {

	private ExchangerHandler handler = null;
	
	public HeaderExchangerHandler(ExchangerHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void connected(Channel channel) throws RemotingException {
		handler.connected(channel);
	}

	@Override
	public void disconnected(Channel channel) throws RemotingException {
		handler.disconnected(channel);
	}

	@Override
	public void sent(Channel channel, Object message) throws RemotingException {
		handler.sent(channel, message);
	}

	@Override
	public void received(Channel channel, Object message) throws RemotingException {
		HeaderExchangerChannel ch = HeaderExchangerChannel.get(channel);
		if(message instanceof Request){
			Request req = (Request)message;
			Response res = new Response(req.getId(), req.getVersion());
			Object result = handler.reply(ch, req.getData());
			if(result!=null && result instanceof Result){
				Result rs = (Result)result;
				if(rs.hasException()){
		            res.setStatus(Response.SERVICE_ERROR);
		            res.setErrorMessage("invoker server method caught exception , cause: " + rs.getException().getMessage());
				}else{
					res.setResult(result);
					res.setStatus(Response.OK);
				}
			}
			channel.send(res);
		}else if(message instanceof Response){
			Response res = (Response)message;
			InvokerFuture.received(res);
		}
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws RemotingException {
		handler.caught(channel, exception);
	}

}
