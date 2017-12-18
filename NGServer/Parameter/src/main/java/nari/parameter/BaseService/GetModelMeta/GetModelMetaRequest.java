package nari.parameter.BaseService.GetModelMeta;

import java.io.Serializable;

public class GetModelMetaRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6329000480972787987L;
	
	String[] modelId = null;
	
	String[] returnField = null;

	public String[] getModelId() {
		return modelId;
	}

	public void setModelId(String[] modelId) {
		this.modelId = modelId;
	}

	public String[] getReturnField() {
		return returnField;
	}

	public void setReturnField(String[] returnField) {
		this.returnField = returnField;
	}
	
}
