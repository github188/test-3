package nari.network.creator.concurrent;

import nari.graph.build.concurrent.ConcurrentGraphBuilder;
import nari.graph.structure.Edge;
import nari.graph.structure.Graph;
import nari.graph.structure.Node;
import nari.network.creator.NetworkBuilder;
import nari.network.device.TopoDevice;
import nari.network.structure.NetworkEdge;
import nari.network.structure.NetworkNode;
import nari.network.structure.concurrent.ConcurrentNetworkEdge;
import nari.network.structure.concurrent.ConcurrentNetworkNode;

public class ConcurrentNetworkBuilder
	extends ConcurrentGraphBuilder implements NetworkBuilder {

	public ConcurrentNetworkBuilder(Graph network) {
		super(network);
	}
	
	@Override
	public NetworkEdge buildNetworkEdge(NetworkNode nodeA, NetworkNode nodeB, long connectionNode) {
		return new ConcurrentNetworkEdge(graph.createComponentId(), nodeA, nodeB, connectionNode);
	}

	@Override
	public NetworkNode buildNetworkNode(TopoDevice device) {
		return new ConcurrentNetworkNode(graph.createComponentId(), device);
	}
	
	@Override
	public Edge buildEdge(Node nodeA, Node nodeB) {
		return new ConcurrentNetworkEdge(graph.createComponentId(), nodeA, nodeB);
	}
	
	@Override
	public Node buildNode() {
		return new ConcurrentNetworkNode(graph.createComponentId());
	}
}
