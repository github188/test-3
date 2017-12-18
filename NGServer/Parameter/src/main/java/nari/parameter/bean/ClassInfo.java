package nari.parameter.bean;

import java.io.Serializable;

public class ClassInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4329177595769734258L;
	
	/**
	 * 设备类型ID
	 */
	private int classId;
	
	/**
	 * 设备类型别名
	 */
	private String classAlias;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassAlias() {
		return classAlias;
	}

	public void setClassAlias(String classAlias) {
		this.classAlias = classAlias;
	}

}
