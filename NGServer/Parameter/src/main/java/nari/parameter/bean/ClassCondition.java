package nari.parameter.bean;

public class ClassCondition {

	private String classId = "";
	
	private String[] DYDJ = null;
	
	private String[] SSXL = null;
	
	private String[] returnField = null;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String[] getDYDJ() {
		return DYDJ;
	}

	public void setDYDJ(String[] dYDJ) {
		DYDJ = dYDJ;
	}

	public String[] getReturnField() {
		return returnField;
	}

	public void setReturnField(String[] returnField) {
		this.returnField = returnField;
	}

	public String[] getSSXL() {
		return SSXL;
	}

	public void setSSXL(String[] sSXL) {
		SSXL = sSXL;
	}
	
}
