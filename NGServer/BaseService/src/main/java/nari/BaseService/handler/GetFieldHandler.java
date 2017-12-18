package nari.BaseService.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.BaseService.BaseServiceActivator;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.parameter.BaseService.GetField.GetFieldRequest;
import nari.parameter.BaseService.GetField.GetFieldResponse;
import nari.parameter.bean.ClassField;
import nari.parameter.bean.Field;
import nari.parameter.code.ReturnCode;

public class GetFieldHandler {
	public GetFieldResponse getField(GetFieldRequest request){
		GetFieldResponse resp = new GetFieldResponse();
		String[] classId = request.getClassId();
		ModelService ms = BaseServiceActivator.modelService;
		DeviceModel model = DeviceModel.NONE;
		ClassField[] classfields = new ClassField[classId.length];
		for(int i=0;i<classId.length;i++){
			classfields[i] = new ClassField();
			classfields[i].setClassId(classId[i]);
			try {
				model = ms.fromClass(classId[i], false);
			} catch (Exception e) {
				e.printStackTrace();
				resp.setCode(ReturnCode.FAILED);
				return resp;
			}
			FieldDef fieldDef= model.getFieldDef();
			Iterator<FieldDetail> it = fieldDef.details();
			List<FieldDetail> fieldList = new ArrayList<FieldDetail>();
			while(it.hasNext()){
				fieldList.add(it.next());
			}
		int count = fieldList.size();
		Field[] fields = new Field[count];
		for(int j=0;j<count;j++){
			fields[j] = new Field();
			fields[j].setFieldName(fieldList.get(j).getFieldName());
			fields[j].setFieldAlias(fieldList.get(j).getFieldAlias());
			fields[j].setDataType(fieldList.get(j).getDataType());	
		}
		classfields[i].setFields(fields);
	}//classid（i）循环结束	
		resp.setClassfields(classfields);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}	
}
