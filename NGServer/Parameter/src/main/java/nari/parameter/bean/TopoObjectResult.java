package nari.parameter.bean;

public class TopoObjectResult {

	private int modelId;
	
	private int[] oids;
	
	private int[] eleStatus;
	
	private String[] termEleStatus;

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public int[] getOids() {
		return oids;
	}

	public void setOids(int[] oids) {
		this.oids = oids;
	}

	public int[] getEleStatus() {
		return eleStatus;
	}

	public void setEleStatus(int[] eleStatus) {
		this.eleStatus = eleStatus;
	}

	public String[] getTermEleStatus() {
		return termEleStatus;
	}

	public void setTermEleStatus(String[] termEleStatus) {
		this.termEleStatus = termEleStatus;
	}
	
}
