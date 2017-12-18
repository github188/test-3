package nari.MemCache;

import nari.MemCache.btree.BTree;

public class NormalFieldIndex extends AbstractFieldIndex {

//	private final ConcurrentMap<Object,PointerRange> map = new ConcurrentHashMap<Object,PointerRange>();
	
	private FieldType fieldType;
	
	private String indexField;
	
	private BTree<String, Integer> tree = null;
	
	private int currentIndex = 0;
	
	private int indexSize = 5;
	
	private IndexArray[] ptrIndex = null;
	
	public NormalFieldIndex(String indexField,FieldType fieldType,PointerCluster ptrCluster) {
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
//		PointerRange range = map.get(key);
//		if(range==null){
//			range = new PointerRange();
//			range.addStartPtr(ptr);
//			map.put(key, range);
//		}
//		
//		Pointer stopPtr = range.getStopPtr();
//		if(stopPtr==null){
//			range.getStartPtr().setNext(indexField, ptr);
//			range.addStopPtr(ptr);
//		}else{
//			stopPtr.setNext(indexField, ptr);
//			range.addStopPtr(ptr);
//		}
		if(key==null){
			return;
		}
		Integer index = tree.get(String.valueOf(key));
		if(index==null){
			tree.put(String.valueOf(key), currentIndex);
			IndexArray arr = new IndexArray(500);
			arr.add(ptr);
			ptrIndex[currentIndex] = arr;
			currentIndex++;
			
			if(currentIndex%indexSize==0){
				IndexArray[] newPtrIndex = new IndexArray[ptrIndex.length+indexSize];
//				int s = ptrIndex.length+indexSize;
//				indexSize = s;
				System.arraycopy(ptrIndex, 0, newPtrIndex, 0, ptrIndex.length);
				ptrIndex = newPtrIndex;
			}
		}else{
			ptrIndex[index].add(ptr);
		}
		
	}

	@Override
	protected void doModify(String keyField,Object key, Pointer ptr) throws Exception {
//		PointerRange range = map.get(key);
//		if(range==null){
//			range = new PointerRange();
//			range.addStartPtr(ptr);
//			map.put(key, range);
//		}else{
//			Pointer stopPtr = range.getStopPtr();
//			if(stopPtr==null){
//				range.getStartPtr().setNext(indexField, ptr);
//				range.addStopPtr(ptr);
//			}else{
//				stopPtr.setNext(indexField, ptr);
//				range.addStopPtr(ptr);
//			}
//		}
//		
//		Object org = ptr.getOriginalObj();
//		Field field = org.getClass().getDeclaredField(keyField);
//		field.setAccessible(true);
//		Object val = field.get(org);
//		
//		range = map.get(val);
//		
//		Pointer p = range.getStartPtr();
//		
//		while(p!=null){
//			if(p==ptr){
//				p.prev(keyField).setNext(keyField, p.next(keyField));
//				p.next(keyField).setPrev(keyField, p.prev(keyField));
//				break;
//			}
//			p = p.next(indexField);
//		}
		
	}

	@Override
	protected void doRemove(String keyField,Object key, Pointer ptr) throws Exception {
//		Object org = ptr.getOriginalObj();
//		Field field = org.getClass().getDeclaredField(keyField);
//		field.setAccessible(true);
//		Object val = field.get(org);
//		
//		PointerRange range = map.get(val);
//		
//		Pointer p = range.getStartPtr();
//		
//		while(p!=null){
//			if(p==ptr){
//				p.prev(keyField).setNext(keyField, p.next(keyField));
//				p.next(keyField).setPrev(keyField, p.prev(keyField));
//				break;
//			}
//			p = p.next(indexField);
//		}
	}
	
	@Override
	protected Pointer[] doGet(Object key,boolean precise) throws Exception {
//		PointerRange range = map.get(key);
//		if(range==null){
//			return new Pointer[]{};
//		}
//		
//		Pointer ptr = range.getStartPtr();
//		
//		List<Pointer> ptrs = new ArrayList<Pointer>();
//		while(ptr!=null){
//			ptrs.add(ptr);
//			ptr = ptr.next(indexField);
//		}
//		Pointer[] array = new Pointer[ptrs.size()];
//		array = ptrs.toArray(array);
		if(key==null){
			return null;
		}
		Integer index = tree.get(String.valueOf(key));
		if(index==null){
			return null;
		}
		return ptrIndex[index].get();
	}

	@Override
	protected String getField() throws Exception {
		return indexField;
	}

	@Override
	public IndexType getIndexType() throws Exception {
		return IndexType.NORMAL;
	}

//	class PointerRange{
//		
//		private Pointer start;
//		
//		private Pointer stop;
//		
//		public PointerRange() {
//			
//		}
//		
//		public Pointer getStartPtr(){
//			return start;
//		}
//		
//		public void addStartPtr(Pointer start){
//			this.start = start;
//		}
//		
//		public Pointer getStopPtr(){
//			return stop;
//		}
//		
//		public void addStopPtr(Pointer stop){
//			this.stop = stop;
//		}
//	}

	@Override
	protected boolean doInit() throws Exception {
		tree = new BTree<String, Integer>();
		ptrIndex = new IndexArray[indexSize];
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

	private class IndexArray{
		
		private int size = 0;
		
		private Pointer[] ptrs = null;
		
		private int currentIndex = 0;
		
		public IndexArray(int size){
			this.size = size;
			ptrs = new Pointer[size];
		}
		
		public void add(Pointer ptr){
			ptrs[currentIndex++] = ptr;
			if(currentIndex%size==0){
				Pointer[] newPtrs = new Pointer[ptrs.length+size];
				System.arraycopy(ptrs, 0, newPtrs, 0, ptrs.length);
				ptrs = newPtrs;
			}
		}
		
		public Pointer[] get(){
			return ptrs;
		}
	}
}
