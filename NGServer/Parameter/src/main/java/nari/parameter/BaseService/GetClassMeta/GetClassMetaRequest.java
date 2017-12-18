package nari.parameter.BaseService.GetClassMeta;

import java.io.Serializable;

public class GetClassMetaRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7432918048378612244L;


	String[] classId = null;
	
	String[] returnField = null;

	public String[] getClassId() {
		return classId;
	}

	public void setClassId(String[] classId) {
		this.classId = classId;
	}

	public String[] getReturnField() {
		return returnField;
	}

	public void setReturnField(String[] returnField) {
		this.returnField = returnField;
	}
	
}

