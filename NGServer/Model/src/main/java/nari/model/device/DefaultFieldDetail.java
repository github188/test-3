package nari.model.device;

import nari.model.bean.FieldDetail;
import nari.model.bean.FieldObject;

public class DefaultFieldDetail implements FieldDetail {

	private FieldObject obj = null;
	
	private FieldProcessor processor = FieldProcessor.NONE;
	
	public DefaultFieldDetail(FieldObject obj,FieldProcessor processor){
		this.obj = obj;
		this.processor = processor;
	}
	
	@Override
	public String getFieldId() {
		return String.valueOf(obj.getFieldDefId());
	}

	@Override
	public String getFieldName() {
		return obj.getFieldName();
	}

	@Override
	public String getFieldAlias() {
		return obj.getFieldAlias();
	}

	@Override
	public int getFieldIndex() {
		return obj.getFieldIndex();
	}

	@Override
	public int getDisplayOrder() {
		return obj.getDisplayOrder();
	}

	@Override
	public String getDataType() {
		return obj.getDataType();
	}

	@Override
	public boolean isVisible() {
		return obj.getVisible()==1;
	}

	@Override
	public boolean isEditable() {
		return obj.getEditable()==1;
	}

	@Override
	public boolean isNullable() {
		return obj.getNullable()==1;
	}

	@Override
	public boolean isCode() {
		return obj.getIsCode()==1;
	}

	@Override
	public int getFieldLength() {
		return obj.getFieldLength();
	}

	@Override
	public FieldProcessor getProcessor() {
		return processor;
	}

}
