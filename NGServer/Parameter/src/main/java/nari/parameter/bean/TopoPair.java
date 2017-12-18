package nari.parameter.bean;

import java.io.Serializable;

public class TopoPair implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7881970885247645418L;

	/**
	 * 端子个数
	 */
	private int nodeNum = 0;
	
	/**
	 * 端子号，按顺序 [100,101,300]
	 */
	private long[] nodes = null;

	public int getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}

	public long[] getNodes() {
		return nodes;
	}

	public void setNodes(long[] nodes) {
		this.nodes = nodes;
	}
	
}
