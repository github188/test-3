package nari.model.device;

public class CNode implements Comparable<CNode>{
	
	private int terminalNode;
	
	private long connectNode;

	public CNode(int terminalNode, long connectNode){
		this.terminalNode = terminalNode;
		this.connectNode = connectNode;
	}

	public final int getTerminalNode() {
		return this.terminalNode;
	}

	public final long getConnectNode() {
		return this.connectNode;
	}

	@Override
	public int compareTo(CNode o) {
		if(o==this){
			return 0;
		}
		if(o.getTerminalNode()<this.getTerminalNode()){
			return 1;
		}
		if(o.getTerminalNode()>this.getTerminalNode()){
			return -1;
		}
		return 0;
	}
}