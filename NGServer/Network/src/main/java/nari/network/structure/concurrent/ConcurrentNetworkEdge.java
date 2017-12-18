package nari.network.structure.concurrent;

import nari.graph.structure.Node;
import nari.graph.structure.concurrent.ConcurrentEdge;
import nari.network.bean.NetworkConnection;
import nari.network.structure.NetworkEdge;
import nari.network.structure.NetworkNode;

public class ConcurrentNetworkEdge
	extends ConcurrentEdge implements NetworkEdge {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1012610330020066448L;
	
	private long connectionNode;
	private NetworkConnection networkConnection;

	public ConcurrentNetworkEdge(int id, Node nodeA, Node nodeB) {
		super(id, nodeA, nodeB);
		networkConnection = ((NetworkNode) nodeA).getDevice().relateTo(((NetworkNode) nodeB).getDevice());
	}
	
	public ConcurrentNetworkEdge(int id, Node nodeA, Node nodeB, long connectionNode) {
		super(id, nodeA, nodeB);
		// networkConnection = [terminalA, terminalB]
		networkConnection = ((NetworkNode) nodeA).getDevice().relateTo(((NetworkNode) nodeB).getDevice());
		this.connectionNode = connectionNode;
	}

	@Override
	public long getConnectionNode() {
		return connectionNode;
	}
	
	@Override
	public void setConnectionNode(long connectionNode) {
		this.connectionNode = connectionNode;
	}

	@Override
	public NetworkConnection getNetworkConnection() {
		return networkConnection;
	}

	@Override
	public int getWeight() {
		return 0;
	}
}
