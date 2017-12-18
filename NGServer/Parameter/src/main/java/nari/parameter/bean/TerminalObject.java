package nari.parameter.bean;

public class TerminalObject {

	private TopoObjectId objectId;
	
	private int[] terminals;

	public int[] getTerminals() {
		return terminals;
	}

	public void setTerminals(int[] terminals) {
		this.terminals = terminals;
	}

	public TopoObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(TopoObjectId objectId) {
		this.objectId = objectId;
	}
	
}
