package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public interface Searcher {

	public Pointer[] search(QueryMatcher matcher,IndexCluster indexCluster) throws Exception;
	
	public Pointer[] preciseSearch(QueryMatcher matcher,IndexCluster indexCluster) throws Exception;
}
