package nari.network.structure.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.graph.structure.Edge;
import nari.graph.structure.Graphable;
import nari.graph.structure.simple.SimpleNode;
import nari.network.device.TopoDevice;
import nari.network.structure.NetworkEdge;
import nari.network.structure.NetworkNode;

public class SimpleNetworkNode
	extends SimpleNode implements NetworkNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1834007119376900231L;
	
	private TopoDevice device;
	
	public SimpleNetworkNode(int id) {
		super(id);
	}

	public SimpleNetworkNode(int id, TopoDevice device) {
		super(id);
		this.device = device;
	}

	@Override
	public TopoDevice getDevice() {
		return device;
	}
	
	@Override
	public void setDevice(TopoDevice device) {
		this.device = device;
	}
	
	@Override
	public Iterator<Graphable> getRelated() {
		// 令NetworkNode的邻接单元仍然为NetworkNode
		List<Graphable> related = new ArrayList<Graphable>();
		List<Edge> edges = getEdges();
		for (Edge edge : edges) {
			if (this == edge.getNodeA()) {
				related.add(edge.getNodeB());
			} else {
				related.add(edge.getNodeA());
			}
		}
		return related.iterator();
	}
	
	@Override
	public Iterator<NetworkNode> getDirectedRelated(int terminal) {
		List<NetworkNode> related = new ArrayList<NetworkNode>();
		List<Edge> edges = getEdges();
		for (Edge edge : edges) {
			if (this == edge.getNodeA()) {
				// edge = [this, edge.nodeB]
				NetworkEdge networkEdge = (NetworkEdge) edge;
				if (terminal == networkEdge.getNetworkConnection().getTerminalA()) {
					related.add((NetworkNode) networkEdge.getNodeB());
				}
			} else {
				// edge = [edge.nodeA, this]
				NetworkEdge networkEdge = (NetworkEdge) edge;
				if (terminal == networkEdge.getNetworkConnection().getTerminalB()) {
					related.add((NetworkNode) networkEdge.getNodeA());
				}
			}
		}
		return related.iterator();
	}
	
	@Override
	public Iterator<NetworkNode> getDirectedRelated(int[] terminals) {
		List<NetworkNode> related = new ArrayList<NetworkNode>();
		List<Edge> edges = getEdges();
		for (Edge edge : edges) {
			if (this == edge.getNodeA()) {
				// edge = [this, edge.nodeB]
				NetworkEdge networkEdge = (NetworkEdge) edge;
				for (int terminal : terminals) {
					if (terminal == networkEdge.getNetworkConnection().getTerminalA()) {
						related.add((NetworkNode) networkEdge.getNodeB());
						break;
					}
				}
			} else {
				// edge = [edge.nodeA, this]
				NetworkEdge networkEdge = (NetworkEdge) edge;
				for (int terminal : terminals) {
					if (terminal == networkEdge.getNetworkConnection().getTerminalB()) {
						related.add((NetworkNode) networkEdge.getNodeA());
						break;
					}
				}
			}
		}
		return related.iterator();
	}

	@Override
	public boolean relateTo(Graphable component) {
		// NetworkNode只与NetworkNode相邻接
		if (component instanceof NetworkNode) {
			NetworkNode networkNode = (NetworkNode) component;
			return null != this.getDevice().relateTo(networkNode.getDevice());
		}
		return false;
	}
	
	@Override
	public int getWeight() {
		if (device.isSingleTerminal()) {
			return 0;
		}
		return 1;
	}
}
