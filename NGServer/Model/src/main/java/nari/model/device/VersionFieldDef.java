package nari.model.device;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.FieldObject;

public class VersionFieldDef implements FieldDef {

	private FieldDef base = FieldDef.NONE;
	
	private FieldDetail addFlagDetail = FieldDetail.NONE;
	
	private final AtomicReference<Map<String,FieldDetail>> ref = new AtomicReference<Map<String,FieldDetail>>();
	
	public VersionFieldDef(FieldDef base){
		this.base = base;
	}
	
	@Override
	public ClassDef getClassDef() {
		return base.getClassDef();
	}

	@Override
	public Iterator<FieldDetail> details() {
		if(ref.get()==null){
			if(addFlagDetail==FieldDetail.NONE){
				FieldObject obj = new FieldObject();
				obj.setClassId(Integer.parseInt(base.getClassDef().getClassId()));
				obj.setDataType("NUMBER");
				obj.setDisplayOrder(40);
				obj.setEditable(1);
				obj.setFieldAlias("版本记录编辑状态");
				obj.setFieldDefId(1);
				obj.setFieldIndex(1);
				obj.setFieldLength(22);
				obj.setFieldName("ADDFLAG");
				obj.setIsCode(0);
				obj.setNullable(1);
				obj.setVisible(1);
				addFlagDetail = new DefaultFieldDetail(obj, FieldProcessor.NONE);
			}
			Iterator<FieldDetail> details = base.details();
			
			Map<String,FieldDetail> detailMap = new HashMap<String,FieldDetail>();
			
			FieldDetail d = null;
			while(details.hasNext()){
				d = details.next();
				detailMap.put(d.getFieldName().toUpperCase(), d);
			}
			detailMap.put("ADDFLAG", addFlagDetail);
			ref.compareAndSet(null, detailMap);
		}
		
		return ref.get().values().iterator();
	}

	@Override
	public FieldDetail find(String fieldName) {
		if(fieldName==null || "".equals(fieldName)){
			return FieldDetail.NONE;
		}
		if(!ref.get().containsKey(fieldName.toUpperCase())){
			return FieldDetail.NONE;
		}
		
		return ref.get().get(fieldName.toUpperCase());
	}

	@Override
	public void addField() {
		base.addField();
	}

}
