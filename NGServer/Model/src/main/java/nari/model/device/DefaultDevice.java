package nari.model.device;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.SubClassDef;

public class DefaultDevice implements Device {

	private final Map<FieldDetail,Object> map = new HashMap<FieldDetail,Object>();
	
	private FieldDef def = FieldDef.NONE;
	
	private ClassDef classDef = ClassDef.NONE;
	
	private SubClassDef[] subClassDefs = new SubClassDef[]{};
	
	private boolean isGeometry = false;
	
	private boolean isTopo = false;
	
	private boolean isVirtual = false;
	
	private Map<String,Object> value = null;
	
	public DefaultDevice(FieldDef def,ClassDef classDef,SubClassDef[] subClassDefs){
		this.def = def;
		this.classDef = classDef;
		this.subClassDefs = subClassDefs;
		init();
	}
	
	public DefaultDevice(FieldDef def,ClassDef classDef,SubClassDef[] subClassDefs,Map<String,Object> value){
		this.def = def;
		this.classDef = classDef;
		this.subClassDefs = subClassDefs;
		this.value = value;
		init();
		initDefault();
	}
	
	private void initDefault(){
		if(value!=null){
			for(Map.Entry<String, Object> entry:value.entrySet()){
				setValue(entry.getKey().toUpperCase(), entry.getValue());
			}
		}
	}
	
	private void init(){
		isGeometry = classDef.isGeometry();
		isVirtual = classDef.isContainer();
		
		for(SubClassDef subClass:subClassDefs){
			isTopo = subClass.isConducting();
			break;
		}
	}
	
	@Override
	public void setValue(FieldDetail field, Object value) {
		map.put(field, value);
	}

	@Override
	public void setValue(String name, Object value) {
		FieldDetail field = def.find(name.toUpperCase());
		if(field==null || field==FieldDetail.NONE){
			return;
		}
		map.put(field, value);
	}

	@Override
	public Object getValue(FieldDetail field) {
		if(field == FieldDetail.NONE){
			return null;
		}
		return map.get(field);
	}

	@Override
	public Object getValue(String name) {
		FieldDetail field = def.find(name.toUpperCase());
		if(field == FieldDetail.NONE){
			return null;
		}
		return map.get(field);
	}

	@Override
	public TopoDevice asTopoDevice() {
		if(!isTopo){
			return TopoDevice.NONE;
		}
		TopoDevice dev = new DefaultTopoDevice(this);
		return dev;
	}

	@Override
	public SpatialDevice asSpatialDevice() {
//		if(!isGeometry){
//			return SpatialDevice.NONE;
//		}
		SpatialDevice dev = new DefaultSpatialDevice(this);
		return dev;
	}
	
	@Override
	public VirtualDevice asVirtualDevice() {
		if(!isVirtual){
			return VirtualDevice.NONE;
		}
		VirtualDevice dev = new DefaultVirtualDevice(this);
		return dev;
	}

	@Override
	public boolean isTopoDevice() {
		return isTopo;
	}

	@Override
	public boolean isGeometryDevice() {
		return isGeometry;
	}

	@Override
	public boolean isVirtualDevice() {
		return isVirtual;
	}

	@Override
	public Iterator<FieldDetail> keyIterator(){
		return map.keySet().iterator();
	}

	@Override
	public ClassDef getClassDef() {
		return classDef;
	}

	@Override
	public SubClassDef getSubClassDef() {
		return (subClassDefs==null || subClassDefs.length==0)?null:subClassDefs[0];
	}
}
