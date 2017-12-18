package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public class NoneSearcher extends AbstractSearcher {

	@Override
	protected Pointer[] doSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		return new Pointer[]{};
	}

	@Override
	public Pointer[] doPreciseSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		return new Pointer[]{};
	}

}
