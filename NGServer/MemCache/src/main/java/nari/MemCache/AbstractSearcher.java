package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public abstract class AbstractSearcher implements Searcher {

	@Override
	public Pointer[] search(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		return doSearch(matcher,indexCluster);
	}

	@Override
	public Pointer[] preciseSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		return doPreciseSearch(matcher,indexCluster);
	}
	
	protected abstract Pointer[] doSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception;
	
	protected abstract Pointer[] doPreciseSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception;
}
