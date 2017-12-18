package nari.network.creator;

import nari.graph.build.GraphBuilder;
import nari.network.device.TopoDevice;
import nari.network.structure.NetworkEdge;
import nari.network.structure.NetworkNode;

public interface NetworkBuilder
	extends GraphBuilder {

	NetworkEdge buildNetworkEdge(NetworkNode nodeA, NetworkNode nodeB, long connectionNode);

	NetworkNode buildNetworkNode(TopoDevice device);
	
}
