package nari.MemCache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nari.MemCache.matcher.QueryMatcher;
import nari.MemCache.matcher.QueryPolygon;

public abstract class AbstractMemCluster implements MemCluster {

	private final long DEFAULT_SIZE = 10*1024*1024;
	
	private MemoryBlock currentBlock = null;
	
	private StringPool pool = null;

	private IndexCluster indexCluster = null;
	
	private boolean empty = true;
	
	private int currentId;
	
	private Cache cache;
	
	public AbstractMemCluster(Cache cache,IndexCluster indexCluster) {
		this.cache = cache;
		this.indexCluster = indexCluster;
	}
	
	@Override
	public boolean init() throws Exception {
		currentId = 0;
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
	public IndexCluster getIndex() throws Exception {
		return indexCluster;
	}
	
	@Override
	public CacheEntry search(QueryMatcher matcher) throws Exception {
		if(matcher==null){
			return null;
		}
		QueryPolygon polygon = matcher.getPolygon();
		
		Pointer[] ptrs = null;
		if(polygon==null){
			Searcher searcher = SearcherManager.selectSearcher("normal");
			ptrs = searcher.search(matcher, indexCluster);
		}else{
			Searcher searcher = SearcherManager.selectSearcher("spatial");
			ptrs = searcher.search(matcher, indexCluster);
		}
		
		return new StdCacheEntry(ptrs);
	}
	
	@Override
	public CacheEntry preciseSearch(QueryMatcher matcher) throws Exception {
		if(matcher==null){
			return null;
		}
		QueryPolygon polygon = matcher.getPolygon();
		
		Pointer[] ptrs = null;
		if(polygon==null){
			Searcher searcher = SearcherManager.selectSearcher("normal");
			ptrs = searcher.preciseSearch(matcher, indexCluster);
		}else{
			Searcher searcher = SearcherManager.selectSearcher("spatial");
			ptrs = searcher.preciseSearch(matcher, indexCluster);
		}
		
		return new StdCacheEntry(ptrs);
	}
	
	@Override
	public Pointer put(Object pair,CacheTicket ticket) throws Exception {
		if(empty){
			MemoryBlock block = newBlock(DEFAULT_SIZE);
			currentBlock = block;
			empty = false;
			pool = new MemoryBytePool(DEFAULT_SIZE,getMemAllocater());
		}
		
		long size = sizeOf(pair);
		if(!currentBlock.hasSpace(size)){
			MemoryBlock block = newBlock(DEFAULT_SIZE);
			currentBlock = block;
		}
		
//		Field[] fields = ticket.getCacheBodyFields();
		Field[] fields = cache.getBeanFields();
		
//		Class<?> wrapperClass = ticket.getWrapperBodyClass();
		
		Class<?> wrapperClass = cache.getWrapperBeanClass();
		Object obj = wrapperClass.newInstance();
		
//		Field[] wrapperField = ticket.getCacheWrapperFields();
		
//		Field[] wrapperField = cache.getWrapperBeanFields();
//		
//		for(Field field:wrapperField){
//			
//			field.set(obj, value);
//		}
		
		List<PoolPointer> pools = new ArrayList<PoolPointer>();
		boolean isBreak = false;
		
		for(Field field:fields){
//			field.setAccessible(true);
			if(field.getType()==String.class){
				String str = (String)field.get(pair);
				if(!pool.hasSpace(str.getBytes().length)){
					pool = new MemoryBytePool(DEFAULT_SIZE,getMemAllocater());
				}
				PoolPointer ppt = pool.addPool(field.getName(), str.getBytes());
				pools.add(ppt);
			}else if(field.getType()==Shape.class){
				Shape shape = (Shape)field.get(pair);
				if(shape==null){
					isBreak = true;
					break;
				}
				int type = shape.getType();
				int[] ele = shape.getElementInfo();
				double[] coords = shape.getCoordinates();
				
				int byteSize = 4 + 4 + 4*ele.length + 4 + 8*coords.length;
				byte[] bts = new byte[byteSize];
				
				int off = 0;
				ByteUtils.int2bytes(type, bts, off);
				off = off + 4;
				
				ByteUtils.int2bytes(ele.length, bts, off);
				off = off + 4;
				
				for(int e:ele){
					ByteUtils.int2bytes(e, bts, off);
					off = off + 4;
				}
				
				ByteUtils.int2bytes(coords.length, bts, off);
				off = off + 4;
				
				for(double c:coords){
					ByteUtils.double2bytes(c, bts, off);
					off = off + 8;
				}
				
				if(!pool.hasSpace(bts.length)){
					pool = new MemoryBytePool(DEFAULT_SIZE,getMemAllocater());
				}
				
				PoolPointer ppt = pool.addPool(field.getName(), bts);
				pools.add(ppt);
			}else if(field.getName().equals("_id")){
				Field f = cache.getWrapperBeanField("_id");
				f.set(obj, currentId);
			}else{
				Field f = cache.getWrapperBeanField(field.getName());
				f.set(obj, field.get(pair));
			}
		}
		
		Pointer ptr = null;
		if(!isBreak){
			ptr = currentBlock.putCache(currentId,obj,cache);
			obj = null;
			currentId++;
			for(PoolPointer ppt:pools){
				ptr.addPoolPointer(ppt);
			}
		}
		
		pools.clear();
		pools=null;
		return ptr;
	}
	
	@Override
	public Pointer[] modify(QueryMatcher matcher, Value value,CacheTicket ticket) throws Exception {
		Searcher searcher = SearcherManager.selectSearcher("normal");
		
		Pointer[] ptrs = searcher.search(matcher, indexCluster);
		
		Object cache = null;
		for(Pointer ptr:ptrs){
			cache = ptr.getCache();
			if(cache==null){
				continue;
			}
			
//			ptr.setOriginalObj(cache);
			
			Set<String> set = value.getKeys();
			if(set==null || set.isEmpty()){
				continue;
			}
			
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				String key = it.next();
				Object val = value.getValue(key);
				String valStr = String.valueOf(val);
				
				if(val.getClass()==int.class || val.getClass()==Integer.class){
					ptr.getMemBlock().putInt(cache, key, Integer.parseInt(valStr));
				}else if(val.getClass()==long.class || val.getClass()==Long.class){
					ptr.getMemBlock().putLong(cache, key, Long.parseLong(valStr));
				}else if(val.getClass()==double.class || val.getClass()==Double.class){
					ptr.getMemBlock().putDouble(cache, key, Double.parseDouble(valStr));
				}else if(val.getClass()==short.class || val.getClass()==Short.class){
					ptr.getMemBlock().putShort(cache, key, Short.parseShort(valStr));
				}else if(val.getClass()==float.class || val.getClass()==Float.class){
					ptr.getMemBlock().putFloat(cache, key, Float.parseFloat(valStr));
				}else if(val.getClass()==boolean.class || val.getClass()==Boolean.class){
					ptr.getMemBlock().putBoolean(cache, key, Boolean.parseBoolean(valStr));
				}else if(val.getClass()==String.class){
					Field f = cache.getClass().getDeclaredField(key);
					f.setAccessible(true);
					String str = (String)f.get(cache);
					
					ptr.getMemBlock().putString(str, key, valStr);
				}else{
					ptr.getMemBlock().putObject(cache, key, val);
				}
			}
		}
		
		return ptrs;
	}
	
	@Override
	public Pointer[] remove(QueryMatcher matcher,CacheTicket ticket) throws Exception {
		Searcher searcher = SearcherManager.selectSearcher("normal");
		
		Pointer[] ptrs = searcher.search(matcher, indexCluster);
		
		for(Pointer ptr:ptrs){
//			Object obj = ptr.getCache();
//			ptr.setOriginalObj(obj);
			ptr.getMemBlock().releaseObject(ptr);
		}
		
		return ptrs;
	}
	
//	private void process(Object obj) throws Exception{
//		Field[] fields = obj.getClass().getDeclaredFields();
//		for(Field field:fields){
//			if(field.getType()==PoolObject.class){
//				field.setAccessible(true);
//				PoolObject po = field.get(obj)==null?null:(PoolObject)field.get(obj);
//			
//				Object value = po.getValue();
//				//将变长字段的值存储在常量池中
//				po.setAddress(address);
//				po.setValue(null);
//				field.set(obj, po);
//			}
//		}
//	
//  }
	
	protected abstract boolean doInit() throws Exception;
	
	protected abstract boolean doStart() throws Exception;
	
	protected abstract boolean doStop() throws Exception;
	
	protected abstract MemoryBlock newBlock(long size) throws Exception;
	
}
