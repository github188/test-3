package nari.MemCache;

public abstract class AbstractCacheHandler implements CacheHandler {

	@Override
	public boolean handle(CacheTicket ticket,NextHandler next) throws Exception {
		doHandle(ticket);
		return next.handle(ticket);
	}
	
	@Override
	public void onPostAdd(HandlerChain chain, String name) throws Exception {
		
	}
	
	@Override
	public void onPostRemove(HandlerChain chain, String name) throws Exception {
		
	}
	@Override
	public void onPreAdd(HandlerChain chain, String name) throws Exception {
		
	}
	
	@Override
	public void onPreRemove(HandlerChain chain, String name) throws Exception {
		
	}
	
	protected abstract void doHandle(CacheTicket ticket) throws Exception;
}
