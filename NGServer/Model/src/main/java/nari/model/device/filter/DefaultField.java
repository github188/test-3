package nari.model.device.filter;

public class DefaultField implements Field {

	private String fieldName;
	
	private Class<?> fieldType;
	
	private FieldProcessor processor;
	
	public DefaultField(String fieldName,Class<?> fieldType,FieldProcessor processor){
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.processor = processor;
	}
	
	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public Class<?> getFieldType() {
		return fieldType;
	}

	@Override
	public Object convert(Object value) {
		return processor.convert(value);
	}

}
