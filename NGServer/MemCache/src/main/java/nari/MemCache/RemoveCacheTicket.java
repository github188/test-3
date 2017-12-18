package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public class RemoveCacheTicket extends CacheTicket {

	private QueryMatcher matcher;
	
	public RemoveCacheTicket(MemCluster cluster/**,Class<?> beanClass,Field[] fields,Class<?> wrapperBeanClass,Field[] wrapperFields**/) {
		super(cluster/**,beanClass,fields,wrapperBeanClass,wrapperFields**/);
	}

	public void addMatcher( QueryMatcher matcher){
		this.matcher = matcher;
	}
	
	public QueryMatcher getMatcher(){
		return matcher;
	}
}
