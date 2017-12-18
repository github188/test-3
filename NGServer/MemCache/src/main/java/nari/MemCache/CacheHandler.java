package nari.MemCache;

public interface CacheHandler {

	public boolean handle(CacheTicket ticket,NextHandler next) throws Exception;
	
	public void onPreAdd(HandlerChain chain,String name) throws Exception;
	
	public void onPostAdd(HandlerChain chain,String name) throws Exception;
	
	public void onPreRemove(HandlerChain chain,String name) throws Exception;
	
	public void onPostRemove(HandlerChain chain,String name) throws Exception;
}
