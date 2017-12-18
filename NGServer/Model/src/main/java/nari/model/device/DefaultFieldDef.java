package nari.model.device;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.FieldObject;

public class DefaultFieldDef implements FieldDef {

	private ClassDef def = ClassDef.NONE;
	
	private final Map<String,FieldDetail> detailMap = new HashMap<String,FieldDetail>();
	
	private static final Map<String,FieldProcessor> processors = new HashMap<String,FieldProcessor>();
	
	static{
		
		processors.put("CONNECTION", new FieldProcessor() {
			
			@Override
			public Object process(Object value) {
				return value;
			}
		});
		
		processors.put("SHAPE", new FieldProcessor() {
			
			@Override
			public Object process(Object value) {
				return value;
			}
		});
	}
	
	public DefaultFieldDef(ClassDef def,List<FieldObject> fields){
		this.def = def;
		init(fields);
	}
	
	private void init(List<FieldObject> fields){
		for(FieldObject field:fields){
			if(!(detailMap.containsKey(field.getFieldName()))){
				FieldDetail detail = new DefaultFieldDetail(field,processors.get(field.getFieldName().toUpperCase()==null?FieldProcessor.NONE:processors.get(field.getFieldName().toUpperCase())));
				detailMap.put(field.getFieldName().toUpperCase(), detail);
			}
		}
	}
	
	@Override
	public ClassDef getClassDef() {
		return def;
	}

	@Override
	public Iterator<FieldDetail> details() {
		return detailMap.values().iterator();
	}

	@Override
	public FieldDetail find(String fieldName) {
		if(isNull(fieldName)){
			return FieldDetail.NONE;
		}
		FieldDetail detail = detailMap.get(fieldName);
		if(detail==null){
			return FieldDetail.NONE;
		}
		return detail;
	}
	
	private boolean isNull(String value){
		if(value==null || "".equals(value)){
			return true;
		}
		return false;
	}

	@Override
	public void addField() {
		// TODO Auto-generated method stub
		
	}
	
}
