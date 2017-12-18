package nari.model.device;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.SubClassDef;

public class MapResultSet implements ResultSet {

	private List<Map<String,Object>> maps;
	
	private FieldDef fieldDef;
	
	private ClassDef classDef;
	
	private SubClassDef[] subClassDef;
	
	public MapResultSet(List<Map<String,Object>> maps,FieldDef fieldDef,ClassDef classDef,SubClassDef[] subClassDef){
		this.maps = maps;
		this.fieldDef = fieldDef;
		this.classDef = classDef;
		this.subClassDef = subClassDef;
	}
	
	@Override
	public Device getSingle() {
		if(maps==null || maps.size()==0){
			return Device.NONE;
		}
		Device dev = new DefaultDevice(fieldDef, classDef, subClassDef,maps==null||maps.size()==0?null:maps.get(0));
		return dev;
	}

	@Override
	public Iterator<Device> resultList() {
		if(maps==null || maps.size()==0){
			return null;
		}
		List<Device> list = new ArrayList<Device>();
		
		for(Map<String,Object> map:maps){
			list.add(new DefaultDevice(fieldDef, classDef, subClassDef,map));
		}
		
		return list.iterator();
	}

}
