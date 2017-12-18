package nari.MemCache;

public interface Updater extends CacheLifecycle{

	public void onMessage(Message message) throws Exception;
	
}
