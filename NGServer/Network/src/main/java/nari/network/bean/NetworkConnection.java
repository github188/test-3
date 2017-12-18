package nari.network.bean;

public class NetworkConnection {

	private int terminalA;
	
	private int terminalB;

	public NetworkConnection(int terminalA, int terminalB) {
		super();
		this.terminalA = terminalA;
		this.terminalB = terminalB;
	}
	
	public int getTerminalA() {
		return terminalA;
	}

	public void setTerminalA(int terminalA) {
		this.terminalA = terminalA;
	}

	public int getTerminalB() {
		return terminalB;
	}
	
	public void setTerminalB(int terminalB) {
		this.terminalB = terminalB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + terminalA;
		result = prime * result + terminalB;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetworkConnection other = (NetworkConnection) obj;
		if (terminalA != other.terminalA)
			return false;
		if (terminalB != other.terminalB)
			return false;
		return true;
	}
}
