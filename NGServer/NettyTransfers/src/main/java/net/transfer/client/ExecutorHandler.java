package net.transfer.client;

public class ExecutorHandler extends AbstractExecutorHandler {

	private ChannelHandler handler = null;
	
	public ExecutorHandler(ChannelHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void connected(Channel channel) throws RemotingException {
		getExecutorService().execute(new ExecutorTask(channel,handler,HandlerState.CONNECTED));
	}

	@Override
	public void disconnected(Channel channel) throws RemotingException {
		getExecutorService().execute(new ExecutorTask(channel,handler,HandlerState.DISCONNECTED));
	}

	@Override
	public void sent(Channel channel, Object message) throws RemotingException {
		getExecutorService().execute(new ExecutorTask(channel,handler,HandlerState.SENT,message));
	}

	@Override
	public void received(Channel channel, Object message) throws RemotingException {
		getExecutorService().execute(new ExecutorTask(channel,handler,HandlerState.RECEIVED,message));
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws RemotingException {
		getExecutorService().execute(new ExecutorTask(channel,handler,HandlerState.CAUGHT,exception));
	}

	class ExecutorTask implements Runnable{

		private ChannelHandler handler = null;
		
		private HandlerState state = null;
		
		private Channel channel;
		
		private Object message = null;
		
		private Throwable exception = null;
		
		public ExecutorTask(Channel channel,ChannelHandler handler,HandlerState state){
			this.channel = channel;
			this.handler = handler;
			this.state = state;
		}
		
		public ExecutorTask(Channel channel,ChannelHandler handler,HandlerState state,Throwable exception){
			this.channel = channel;
			this.handler = handler;
			this.state = state;
			this.exception = exception;
		}
		
		public ExecutorTask(Channel channel,ChannelHandler handler,HandlerState state,Object message){
			this.channel = channel;
			this.handler = handler;
			this.state = state;
			this.message = message;
		}
		
		@Override
		public void run() {
			switch (state) {
				case CONNECTED:
					try {
						handler.connected(channel);
					} catch (RemotingException e) {
						e.printStackTrace();
					}
					break;
				case DISCONNECTED:
					try {
						handler.disconnected(channel);
					} catch (RemotingException e) {
						e.printStackTrace();
					}
					break;
				case RECEIVED:
					try {
						handler.received(channel,message);
					} catch (RemotingException e) {
						e.printStackTrace();
					}
					break;
				case SENT:
					try {
						handler.sent(channel, message);
					} catch (RemotingException e) {
						e.printStackTrace();
					}
					break;
				case CAUGHT:
					try {
						handler.caught(channel, exception);
					} catch (RemotingException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	}
	
	enum HandlerState{
		CONNECTED,DISCONNECTED,SENT,RECEIVED,CAUGHT
	}
}
