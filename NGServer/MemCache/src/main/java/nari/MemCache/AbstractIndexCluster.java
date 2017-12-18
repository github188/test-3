package nari.MemCache;

import java.util.Arrays;
import java.util.Set;

public abstract class AbstractIndexCluster implements IndexCluster {

//	private final Map<String,FieldIndex> indexMap = new HashMap<String,FieldIndex>();
	
//	private final List<Pointer> ptrs = new ArrayList<Pointer>();
	
	private String[] indexKeys = new String[2];
	
	private FieldIndex[] indexVals = new FieldIndex[2];
	
	private int idx = 0;
	
	public AbstractIndexCluster() {
		
	}
	
	@Override
	public void register(String indexField, IndexType indexType,FieldType fieldType,PointerCluster ptrCluster) throws Exception {
		FieldIndex index = null;
		switch (indexType) {
			case NORMAL:
				index = new NormalFieldIndex(indexField.toLowerCase(),fieldType,ptrCluster);
				break;
			case UNIQUE:
				index = new UniqueFieldIndex(indexField.toLowerCase(),fieldType,ptrCluster);
				break;
			case SPATIAL:
				index = new SpatialFieldIndex(indexField.toLowerCase(),fieldType,ptrCluster);
				break;
			default:
				break;
		}
		
		if(index==null){
			throw new RuntimeException("unknow index type");
		}
		
		index.init();
		
		index.start();
		
		indexKeys[idx] = indexField.toLowerCase();
		
		indexVals[idx] = index;
		
		idx++;
		
		if(idx%2==0){
			String[] newIndexKeys = new String[indexKeys.length+2];
			System.arraycopy(indexKeys, 0, newIndexKeys, 0, indexKeys.length);
			indexKeys = newIndexKeys;
			
			FieldIndex[] newIndexVals = new FieldIndex[indexVals.length+2];
			System.arraycopy(indexVals, 0, newIndexVals, 0, indexVals.length);
			indexVals = newIndexVals;
		}
		
//		indexMap.put(indexField.toLowerCase(), index);
	}

	@Override
	public void add(String indexField, Pointer ptr) throws Exception{
//		FieldIndex index = indexMap.get(indexField.toLowerCase());
		FieldIndex index = findIndex(indexField);
		Object key = ptr.getFieldValue(indexField,index.getFieldType());
//		Object key = null;
//		ptrs.add(ptr);
		
//		PointerCluster ptrClu = getPointerCluster();
//		ptrClu.addPointer(ptr.getPointerId(), ptr);
		
		index.add(key, ptr);
	}
	
	@Override
	public void modify(Pointer ptr, Value value) throws Exception {
		Set<String> keys = value.getKeys();
		
		FieldIndex index = null;
		for(String key:keys){
//			index = indexMap.get(key.toLowerCase());
			index = findIndex(key);
			if(index==null){
				continue;
			}
			index.modify(key,ptr.getFieldValue(key, index.getFieldType()), ptr);
		}
		
	}
	
	@Override
	public void remove(Pointer ptr) throws Exception {
//		FieldIndex index = null;
//		for(Map.Entry<String, FieldIndex> entry:indexMap.entrySet()){
//			index = entry.getValue();
//			index.remove(entry.getKey(),ptr.getFieldValue(entry.getKey(), index.getFieldType()), ptr);
//		}
//		int i = 0;
//		for(FieldIndex index:indexVals){
//			index.remove(indexKeys[i],ptr.getFieldValue(indexKeys[i], index.getFieldType()), ptr);
//			i++;
//		}
		FieldIndex index = null;
		for(int i=0;i<idx;i++){
			index = indexVals[i];
			index.remove(indexKeys[i],ptr.getFieldValue(indexKeys[i], index.getFieldType()), ptr);
			i++;
		}
	}
	
	@Override
	public String[] getIndexFields() throws Exception {
//		String[] arr = new String[indexMap.keySet().size()];
//		arr = indexMap.keySet().toArray(arr);
		
		return Arrays.copyOfRange(indexKeys, 0, idx);
		
//		return indexKeys;
	}
	
	@Override
	public FieldIndex getIndex(String indexField) throws Exception {
//		return indexMap.get(indexField);
		return findIndex(indexField);
	}
	
	private FieldIndex findIndex(String indexField) throws Exception{
		for(int i=0;i<idx;i++){
			if(indexKeys[i]==null){
				break;
			}
			if(indexKeys[i].equals(indexField.toLowerCase())){
				return indexVals[i];
			}
		}
		return null;
	}
	
	public abstract PointerCluster getPointerCluster() throws Exception ;
	
}
