package nari.MemCache;

public class StdCacheEntry implements CacheEntry {

//	private List<Object> pairList = null;
	
	private int index = 0;
	
	private Pointer[] ptrs = null;

	public StdCacheEntry(Pointer[] ptrs) {
		this.ptrs = ptrs;
	}
	
	@Override
	public boolean hasNext() throws Exception {
		return ptrs==null?false:index<ptrs.length;
	}

	@Override
	public int size() throws Exception {
		return ptrs==null?0:ptrs.length;
	}

	@Override
	public Pointer next() throws Exception {
		return ptrs[index++];
	}
	
//	public StdCacheEntry(List<Object> pairList) {
//		this.pairList = pairList;
//	}
	
//	@Override
//	public boolean hasNext() throws Exception {
//		return index<pairList.size();
//	}
//
//	@Override
//	public int size() throws Exception {
//		return pairList.size();
//	}
//
//	@Override
//	public Object next() throws Exception {
//		return pairList.get(index++);
//	}
	
}
