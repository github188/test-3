package nari.MemCache;

import nari.MemCache.btree.BTree;

public class UniqueFieldIndex extends AbstractFieldIndex {

//	private final ConcurrentMap<Object,Pointer> map = new ConcurrentHashMap<Object,Pointer>();
	
	private FieldType fieldType;
	
	private String indexField;
	
	private BTree<String, Integer> tree = null;
	
	public UniqueFieldIndex(String indexField,FieldType fieldType,PointerCluster ptrCluster) {
		super(ptrCluster);
		this.indexField = indexField;
		this.fieldType = fieldType;
	}
	
	@Override
	public FieldType getFieldType() throws Exception {
		return fieldType;
	}

	@Override
	protected void doAdd(Object key, Pointer ptr) throws Exception {
//		map.put(key, ptr);
		
		if(key==null){
			return;
		}
		tree.put(String.valueOf(key), ptr.getPointerId());
	}

	@Override
	protected Pointer[] doGet(Object key,boolean precise) throws Exception {
//		return new Pointer[]{map.get(key)};
		if(key==null){
			return null;
		}
		int ptrId = tree.get(String.valueOf(key));
		Pointer[] ptr = new Pointer[]{getPointerCluster().getPointer(ptrId)};
		return ptr;
	}

	@Override
	public IndexType getIndexType() throws Exception {
		return IndexType.UNIQUE;
	}

	@Override
	protected String getField() throws Exception {
		return indexField;
	}

	@Override
	protected void doModify(String keyField,Object key, Pointer ptr) throws Exception {
//		map.put(key, ptr);
//		
//		Object org = ptr.getOriginalObj();
//		Field field = org.getClass().getDeclaredField(keyField);
//		field.setAccessible(true);
//		Object val = field.get(org);
//		
//		map.remove(val);
	}

	@Override
	protected void doRemove(String keyField,Object key, Pointer ptr) throws Exception {
//		map.remove(key);
	}

	@Override
	protected boolean doInit() throws Exception {
		tree = new BTree<String, Integer>();
		return true;
	}

	@Override
	protected boolean doStart() throws Exception {
		return true;
	}

	@Override
	protected boolean doStop() throws Exception {
		return true;
	}

	@Override
	protected void doPersistence() throws Exception {
		
	}
	
}
