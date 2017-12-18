package nari.MemCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Value {

	private final Map<String,Object> map = new HashMap<String,Object>();
	
	public static Value val(){
		return new Value();
	}
	
	public Value add(String key,int val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,long val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,short val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,double val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,float val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,boolean val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,byte val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,String val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Value add(String key,Object val) throws Exception{
		map.put(key, val);
		return this;
	}
	
	public Set<String> getKeys() throws Exception{
		return map.keySet();
	}
	
	public Object getValue(String key) throws Exception{
		return map.get(key);
	}
}
