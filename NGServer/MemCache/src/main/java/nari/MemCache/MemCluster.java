package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public interface MemCluster extends CacheLifecycle{

	public CacheEntry search(QueryMatcher matcher) throws Exception;
	
	public CacheEntry preciseSearch(QueryMatcher matcher) throws Exception;
	
	public MemAllocater getMemAllocater() throws Exception;
	
	public Pointer put(Object pair,CacheTicket ticket) throws Exception;
	
	public Pointer[] modify(QueryMatcher matcher, Value value,CacheTicket ticket) throws Exception;
	
	public Pointer[] remove(QueryMatcher matcher,CacheTicket ticket) throws Exception;
	
	public long sizeOf(Object obj) throws Exception;
	
	public IndexCluster getIndex() throws Exception;
}
