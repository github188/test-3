package nari.model.bean;

public class FieldObject {

	private int classId;
	
	private int fieldDefId;
	
	private String fieldName;
	
	private String fieldAlias;
	
	private int fieldIndex;
	
	private int displayOrder;
	
	private String dataType;
	
	private int visible;
	
	private int editable;
	
	private int nullable;
	
	private int isCode;
	
	private int fieldLength;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getFieldDefId() {
		return fieldDefId;
	}

	public void setFieldDefId(int fieldDefId) {
		this.fieldDefId = fieldDefId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldAlias() {
		return fieldAlias;
	}

	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}

	public int getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex(int fieldIndex) {
		this.fieldIndex = fieldIndex;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getEditable() {
		return editable;
	}

	public void setEditable(int editable) {
		this.editable = editable;
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public int getIsCode() {
		return isCode;
	}

	public void setIsCode(int isCode) {
		this.isCode = isCode;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

}
