package nari.MemCache;

public abstract class AbstractFieldIndex implements FieldIndex {

	private PointerCluster ptrCluster;
	
	public AbstractFieldIndex(PointerCluster ptrCluster) {
		this.ptrCluster = ptrCluster;
	}
	
	@Override
	public void add(Object indexKey, Pointer ptr) throws Exception {
//		IndexKey key = IndexKey.key(indexKey);
		
		doAdd(indexKey,ptr);
		
//		ptr.addFieldIndex(getIndexField(), this);
//		ptr.addIndexKey(getIndexField(), indexKey);
	}
	
//	@Override
//	public void add(Object indexKey, int ptrId) throws Exception {
//		doAdd(indexKey,ptrId);
//		
////		ptr.addFieldIndex(getIndexField(), this);
//		ptr.addIndexKey(getIndexField(), indexKey);
//	}
	
	@Override
	public void modify(String keyField,Object indexKey, Pointer ptr) throws Exception {
//		IndexKey key = IndexKey.key(indexKey);
		doModify(keyField,indexKey,ptr);
		ptr.addIndexKey(getIndexField(), indexKey);
	}
	
	@Override
	public void remove(String keyField,Object indexKey, Pointer ptr) throws Exception {
//		IndexKey key = IndexKey.key(indexKey);
		doRemove(keyField,indexKey,ptr);
		ptr.release();
		ptr = null;
	}
	
	@Override
	public Pointer[] get(Object indexKey) throws Exception {
//		IndexKey key = IndexKey.key(indexKey);
		return doGet(indexKey,false);
	}
	
	@Override
	public Pointer[] get(Object indexKey, boolean precise) throws Exception {
		return doGet(indexKey,precise);
	}
	
	@Override
	public String getIndexField() throws Exception {
		return getField();
	}
	
	@Override
	public boolean init() throws Exception {
		return doInit();
	}
	
	@Override
	public boolean start() throws Exception {
		return doStart();
	}
	
	@Override
	public boolean stop() throws Exception {
		return doStop();
	}
	
	@Override
	public void persistence() throws Exception {
		
	}
	
	protected PointerCluster getPointerCluster() {
		return ptrCluster;
	}
	
	protected abstract boolean doInit() throws Exception;
	
	protected abstract boolean doStart() throws Exception;
	
	protected abstract boolean doStop() throws Exception;
	
	public abstract IndexType getIndexType() throws Exception;
	
	protected abstract Pointer[] doGet(Object key,boolean precise) throws Exception;
	
	protected abstract void doAdd(Object key, Pointer ptr) throws Exception;
	
	protected abstract void doModify(String keyField,Object key, Pointer ptr) throws Exception;
	
	protected abstract void doRemove(String keyField,Object key, Pointer ptr) throws Exception;
	
	protected abstract String getField() throws Exception;
	
	protected abstract void doPersistence() throws Exception;
	
}
