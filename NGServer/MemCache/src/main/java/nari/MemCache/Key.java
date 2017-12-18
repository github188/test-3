package nari.MemCache;

public class Key {

	private String key;
	
	private Key(String key) {
		this.key = key;
	}
	
	public static Key val(String key){
		return new Key(key);
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Key)){
			return false;
		}
		
		Key k = (Key)obj;
		
		if(k.getKey().equalsIgnoreCase(key)){
			return true;
		}
		return false;
	}
	
	public String getKey(){
		return key;
	}
	
}
