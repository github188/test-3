package nari.MemCache;

import java.lang.reflect.Field;

import nari.Logger.LoggerManager;

public class WrapperMemPointer implements Pointer {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private Pointer pointer;
	
	private MemoryBlock block;
	
	private int id;
	
//	@SuppressWarnings("unused")
//	private Class<?> cacheClass;
	
//	private Field[] fields;
	
	private MemAllocater allocater;
	
//	private Map<String,Field> fieldClass;
	
	private Cache cache;
	
	public WrapperMemPointer(int id,Pointer ptr,MemoryBlock block,Cache cache,MemAllocater allocater) {
		this.id = id;
		this.pointer = ptr;
		this.block = block;
		this.cache = cache;
		this.allocater = allocater;
//		try {
//			this.cacheClass = cache.getBeanClass();
//			Field[] fields = cache.getBeanFields();
//			fieldClass = new HashMap<String,Field>(fields.length);
//			for(Field f:fields){
//				fieldClass.put(f.getName(), f);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public long getAddress() throws Exception {
		return pointer.getAddress();
	}

	@Override
	public long getSize() throws Exception {
		return pointer.getSize();
	}

	@Override
	public MemoryBlock getMemBlock() throws Exception {
		return block;
	}

//	@Override
//	public void addFieldIndex(String fieldName, FieldIndex index) throws Exception {
//		pointer.addFieldIndex(fieldName, index);
//	}

	@Override
	public Object getFieldValue(String field,FieldType fieldType) throws Exception {
		
		Object cache = pointer.getCache();
		
		Object value = null;
//		Field f = fieldClass.get(field);
//		Field f = this.cache.getBeanField(field);
		Field f = this.cache.getWrapperBeanField(field);
//		Field f = cache.getClass().getDeclaredField(field);
//		f.setAccessible(true);
		
		if(f==null){
			logger.info(field);
		}
		if(f.getType()==int.class || f.getType()==Integer.class){
			value = block.getInt(cache, f);
		}else if(f.getType()==long.class || f.getType()==Long.class){
			value = block.getLong(cache, f);
		}else if(f.getType()==double.class || f.getType()==Double.class){
			value = block.getDouble(cache, f);
		}else if(f.getType()==short.class || f.getType()==Short.class){
			value = block.getShort(cache, f);
		}else if(f.getType()==float.class || f.getType()==Float.class){
			value = block.getFloat(cache, f);
		}else if(f.getType()==String.class){
			PoolPointer ppt = pointer.getPoolPointer(field);
			
			byte[] bytes = new byte[ppt.size()];
			for(int i=0;i<ppt.size();i++){
				bytes[i] = allocater.getByte(ppt.address()+i);
			}
			value = new String(bytes);
		}else if(f.getType()==Shape.class){
			PoolPointer ppt = pointer.getPoolPointer(field);
			
			byte[] bytes = new byte[ppt.size()];
			for(int i=0;i<ppt.size();i++){
				bytes[i] = allocater.getByte(ppt.address()+i);
			}
			
			int off = 0;
			int type = ByteUtils.bytes2int(bytes, off);
			off = off + 4;
			int eleSize = ByteUtils.bytes2int(bytes, off);
			off = off + 4;
			
			int[] ele = new int[eleSize];
			int k = 0;
			for(int i=0;i<eleSize;i++){
				int e = ByteUtils.bytes2int(bytes, off);
				ele[k++] = e;
				off = off + 4;
			}
			
			int coordSize = ByteUtils.bytes2int(bytes, off);
			off = off + 4;
			
			double[] coords = new double[coordSize];
			k = 0;
			
			for(int i=0;i<coordSize;i++){
				double coord = ByteUtils.bytes2double(bytes, off);
				coords[k++] = coord;
				off = off + 8;
			}
			
			Shape shape = new Shape();
			shape.setType(type);
			
			shape.setElementInfo(ele);
			
			shape.setCoordinates(coords);
			value = shape;
		}
		
		cache = null;
		return value;
	}

//	@Override
//	public Pointer next(String indexField) throws Exception {
//		return pointer.next(indexField);
//	}
//
//	@Override
//	public void setNext(String indexField, Pointer ptr) throws Exception {
//		pointer.setNext(indexField, ptr);
//	}

	@Override
	public void addIndexKey(String fieldName, Object key) throws Exception {
		pointer.addIndexKey(fieldName, key);
	}

	@Override
	public Object getIndexKey(String fieldName) throws Exception {
		return pointer.getIndexKey(fieldName);
	}

	@Override
	public Object getCache() throws Exception {
		Object obj = pointer.getCache();
		
//		for(Field field:fields){
//			if(field.getType()==String.class){
//				PoolPointer ppt = pointer.getPoolPointer(field.getName());
//				
//				byte[] bytes = new byte[ppt.size()];
//				for(int i=0;i<ppt.size();i++){
//					bytes[i] = allocater.getByte(ppt.address()+i);
//				}
//				String s = new String(bytes);
//				field.set(obj, s);
//			}else if(field.getType()==Shape.class){
//				
//				PoolPointer ppt = pointer.getPoolPointer(field.getName());
//				
//				byte[] bytes = new byte[ppt.size()];
//				for(int i=0;i<ppt.size();i++){
//					bytes[i] = allocater.getByte(ppt.address()+i);
//				}
//				
//				int off = 0;
//				int type = ByteUtils.bytes2int(bytes, off);
//				off = off + 4;
//				int eleSize = ByteUtils.bytes2int(bytes, off);
//				off = off + 4;
//				
//				int[] ele = new int[eleSize];
//				int k = 0;
//				for(int i=0;i<eleSize;i++){
//					int e = ByteUtils.bytes2int(bytes, off);
//					ele[k++] = e;
//					off = off + 4;
//				}
//				
//				int coordSize = ByteUtils.bytes2int(bytes, off);
//				off = off + 4;
//				
//				double[] coords = new double[coordSize];
//				k = 0;
//				
//				for(int i=0;i<coordSize;i++){
//					double coord = ByteUtils.bytes2double(bytes, off);
//					coords[k++] = coord;
//					off = off + 8;
//				}
//				
//				Shape shape = new Shape();
//				shape.setType(type);
//				
//				shape.setElementInfo(ele);
//				
//				shape.setCoordinates(coords);
//				
//				field.set(obj, shape);
//				
//			}
//		}
		
		return obj;
	}

//	@Override
//	public void setOriginalObj(Object obj) throws Exception {
//		pointer.setOriginalObj(obj);
//	}
//
//	@Override
//	public Object getOriginalObj() throws Exception {
//		return pointer.getOriginalObj();
//	}

//	@Override
//	public Pointer prev(String indexField) throws Exception {
//		return pointer.prev(indexField);
//	}
//
//	@Override
//	public void setPrev(String indexField, Pointer ptr) throws Exception {
//		pointer.setPrev(indexField, ptr);
//	}

	@Override
	public void release() throws Exception {
		pointer.release();
	}

	@Override
	public int getPointerId() {
		return id;
	}

	@Override
	public void addPoolPointer(PoolPointer ppt) throws Exception {
		pointer.addPoolPointer(ppt);
	}

	@Override
	public PoolPointer getPoolPointer(String field) throws Exception {
		return pointer.getPoolPointer(field);
	}

	@Override
	public Cache getMemCache() throws Exception {
		return cache;
	}

	@Override
	public void setFieldValue(String field, FieldType fieldType, Object val) throws Exception {
		
	}

}
