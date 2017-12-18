package nari.MemCache;

import javax.naming.OperationNotSupportedException;

public class MemPointer implements Pointer {

	private long address;
	
	private long size;
	
	private MemAllocater allocater;
	
//	private final Map<String,Object> map = new HashMap<String,Object>();
	
//	private final int KEYSIZE = 3;
	
//	private String[] indexKeys = new String[KEYSIZE];
//	
//	private Object[] indexValues = new Object[KEYSIZE];
	
//	private int index = 0;
	
//	private final Map<String,Pointer> nextMap = new HashMap<String,Pointer>();
//	
//	private final Map<String,Pointer> prevMap = new HashMap<String,Pointer>();
	
//	private final Map<String,PoolPointer> pptMap = new HashMap<String,PoolPointer>();
	
	private String[] keys = new String[2];
	
	private PoolPointer[] ppts = new PoolPointer[2];
	
	private int index = 0;
	
//	private Object originalObj = null;
	
	public MemPointer(long address,long size,MemAllocater allocater) {
		this.address = address;
		this.size = size;
		this.allocater = allocater;
	}
	
	@Override
	public long getAddress() throws Exception{
		return address;
	}
	
	@Override
	public long getSize() throws Exception{
		return size;
	}

	@Override
	public MemoryBlock getMemBlock() throws Exception {
		throw new OperationNotSupportedException();
	}

//	@Override
//	public void addFieldIndex(String fieldName, FieldIndex index) throws Exception {
//		map.put(fieldName, index);
//	}

	@Override
	public Object getFieldValue(String field,FieldType fieldType) throws Exception {
		throw new OperationNotSupportedException();
	}

//	@Override
//	public Pointer next(String indexField) throws Exception {
//		return nextMap.get(indexField);
//	}
//
//	@Override
//	public void setNext(String indexField, Pointer ptr) throws Exception {
//		nextMap.put(indexField, ptr);
//	}

	@Override
	public void addIndexKey(String fieldName, Object key) throws Exception {
//		if(key instanceof Shape){
//			return;
//		}
////		map.put(fieldName, key);
//		
//		if(index+1>indexKeys.length){
//			int s = indexKeys.length;
//			String[] newIndexKeys = new String[s*2];
//			System.arraycopy(indexKeys, 0, newIndexKeys, 0, s);
//			indexKeys = newIndexKeys;
//			
//			Object[] newIndexValues = new Object[s*2];
//			System.arraycopy(indexValues, 0, newIndexValues, 0, s);
//			indexValues = newIndexValues;
//		}
//		indexKeys[index] = fieldName;
//		indexValues[index] = key;
//		index++;
	}

	@Override
	public Object getIndexKey(String fieldName) throws Exception {
//		int i=0;
//		for(String key:indexKeys){
//			if(key.equals(fieldName)){
//				return indexValues[i];
//			}
//			i++;
//		}
		return null;
	}

	@Override
	public Object getCache() throws Exception {
		return allocater.getCache(address);
	}

//	@Override
//	public void setOriginalObj(Object obj) throws Exception {
//		this.originalObj = obj;
//	}
//
//	@Override
//	public Object getOriginalObj() throws Exception {
//		return originalObj;
//	}

//	@Override
//	public Pointer prev(String indexField) throws Exception {
//		return prevMap.get(indexField);
//	}
//
//	@Override
//	public void setPrev(String indexField, Pointer ptr) throws Exception {
//		prevMap.put(indexField, ptr);
//	}

	@Override
	public void release() throws Exception {
//		map.clear();
//		nextMap.clear();
//		prevMap.clear();
	}

	@Override
	public int getPointerId() {
		return -1;
	}

	@Override
	public void addPoolPointer(PoolPointer ppt) throws Exception {
		keys[index] = ppt.getFieldName();
		ppts[index] = ppt;
		index++;
		if(index%2==0){
			String[] newKeys = new String[keys.length+2];
			System.arraycopy(keys, 0, newKeys, 0, keys.length);
			
			keys = newKeys;
			
			PoolPointer[] newPpts = new PoolPointer[ppts.length+2];
			System.arraycopy(ppts, 0, newPpts, 0, ppts.length);
			
			ppts = newPpts;
		}
		
//		pptMap.put(ppt.getFieldName(), ppt);
	}

	@Override
	public PoolPointer getPoolPointer(String field) throws Exception {
		for(int i=0;i<keys.length;i++){
			if(keys[i].equals(field)){
				return ppts[i];
			}
		}
		
		return null;
	}

	@Override
	public Cache getMemCache() throws Exception {
		return null;
	}

	@Override
	public void setFieldValue(String field, FieldType fieldType, Object val) throws Exception {
		throw new OperationNotSupportedException();
	}
	
}
