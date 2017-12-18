package nari.parameter.bean;

import nari.parameter.bean.Field;

public class ClassField {

	private String classId = "";
	
	private Field[] fields = null;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}
	
}
