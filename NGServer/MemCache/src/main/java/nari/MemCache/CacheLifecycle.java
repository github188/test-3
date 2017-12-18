package nari.MemCache;

public interface CacheLifecycle {

	public boolean init() throws Exception;
	
	public boolean start() throws Exception;
	
	public boolean stop() throws Exception;
}
