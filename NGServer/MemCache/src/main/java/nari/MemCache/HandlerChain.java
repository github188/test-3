package nari.MemCache;

public interface HandlerChain {

	public void addFirst(String name,CacheHandler handler) throws Exception;
	
	public void addLast(String name,CacheHandler handler) throws Exception;
	
	public void remove(String name) throws Exception;
	
	public boolean handle(CacheTicket ticket) throws Exception;
	
	public void buildHandlerChain() throws Exception;
}
