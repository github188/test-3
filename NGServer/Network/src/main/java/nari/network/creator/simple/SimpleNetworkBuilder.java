package nari.network.creator.simple;

import nari.graph.build.simple.SimpleGraphBuilder;
import nari.graph.structure.Edge;
import nari.graph.structure.Graph;
import nari.graph.structure.Node;
import nari.network.creator.NetworkBuilder;
import nari.network.device.TopoDevice;
import nari.network.structure.NetworkEdge;
import nari.network.structure.NetworkNode;
import nari.network.structure.simple.SimpleNetworkEdge;
import nari.network.structure.simple.SimpleNetworkNode;

public class SimpleNetworkBuilder
	extends SimpleGraphBuilder implements NetworkBuilder {

	public SimpleNetworkBuilder(Graph network) {
		super(network);
	}

	@Override
	public NetworkEdge buildNetworkEdge(NetworkNode nodeA, NetworkNode nodeB, long connectionNode) {
		return new SimpleNetworkEdge(graph.createComponentId(), nodeA, nodeB, connectionNode);
	}

	@Override
	public NetworkNode buildNetworkNode(TopoDevice device) {
		return new SimpleNetworkNode(graph.createComponentId(), device);
	}
	
	@Override
	public Edge buildEdge(Node nodeA, Node nodeB) {
		return new SimpleNetworkEdge(graph.createComponentId(), nodeA, nodeB);
	}
	
	@Override
	public Node buildNode() {
		return new SimpleNetworkNode(graph.createComponentId());
	}
}
