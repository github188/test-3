package nari.parameter.bean;

import java.io.Serializable;

public class PSRCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5713642430148297670L;

	private String classId = null;
	
	private SubPSRCondition[] modelCondition = null;
	
	

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public SubPSRCondition[] getModelCondition() {
		return modelCondition;
	}

	public void setModelCondition(SubPSRCondition[] modelCondition) {
		this.modelCondition = modelCondition;
	}

	
	
}
