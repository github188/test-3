package nari.SpatialIndex.searcher;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerialObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1617634812938375425L;

	public static final String GEOMETRY = "geometry";
	
//	private Geometry geom = null;
	
	private byte[] data = null;
	
	private final Map<String,Object> map = new HashMap<String,Object>();
	
	public Object getValue(String key){
		return map.get(key);
	}
	
	public void setValue(String key,Object value){
		map.put(key, value);
	}
	
//	public Geometry getGeometry(){
//		return geom;
//	}
//	
//	public void setGeometry(Geometry geom){
//		this.geom = geom;
//	}
	
	public byte[] getData(){
		return data;
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
}
