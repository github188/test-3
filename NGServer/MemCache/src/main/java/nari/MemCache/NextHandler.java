package nari.MemCache;

public interface NextHandler {

	public boolean handle(CacheTicket ticket) throws Exception;
}
