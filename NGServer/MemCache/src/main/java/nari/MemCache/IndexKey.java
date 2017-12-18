package nari.MemCache;

public class IndexKey {

	public static final IndexKey NONE = new IndexKey(null);
	
	private Object key;
	
	private IndexKey(Object key){
		this.key = key;
	}
	
	public static IndexKey key(Object indexKey) throws Exception{
		if(indexKey==null){
			return NONE;
		}
		return new IndexKey(indexKey);
	}
	
	public Object getKey(){
		return key;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if(obj instanceof IndexKey){
			return ((IndexKey)obj).getKey().equals(key);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return key==null?0:key.hashCode();
	}
	
}
