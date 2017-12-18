package nari.MemCache;

public class AddCacheTicket extends CacheTicket {

	private Object pair;
	
	public AddCacheTicket(MemCluster cluster/**,Class<?> beanClass,Field[] fields,Class<?> wrapperBeanClass,Field[] wrapperFields**/) {
		super(cluster/**,beanClass,fields,wrapperBeanClass,wrapperFields**/);
	}

	public void addPair(Object pair) throws Exception{
		this.pair = pair;
	}
	
	public Object getPair() throws Exception{
		return pair;
	}
}
