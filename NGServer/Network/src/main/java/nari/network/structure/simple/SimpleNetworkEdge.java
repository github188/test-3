package nari.network.structure.simple;

import nari.graph.structure.Node;
import nari.graph.structure.simple.SimpleEdge;
import nari.network.bean.NetworkConnection;
import nari.network.structure.NetworkEdge;
import nari.network.structure.NetworkNode;

public class SimpleNetworkEdge
	extends SimpleEdge implements NetworkEdge {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2789076241742294491L;
	
	private long connectionNode;
	private NetworkConnection networkConnection;
	
	public SimpleNetworkEdge(int id, Node nodeA, Node nodeB) {
		super(id, nodeA, nodeB);
		networkConnection = ((NetworkNode) nodeA).getDevice().relateTo(((NetworkNode) nodeB).getDevice());
	}

	public SimpleNetworkEdge(int id, Node nodeA, Node nodeB, long connectionNode) {
		super(id, nodeA, nodeB);
		this.connectionNode = connectionNode;
		networkConnection = ((NetworkNode) nodeA).getDevice().relateTo(((NetworkNode) nodeB).getDevice());
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
