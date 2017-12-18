package nari.graph.build.concurrent;

import nari.graph.build.GraphBuilder;
import nari.graph.structure.Edge;
import nari.graph.structure.Graph;
import nari.graph.structure.Node;
import nari.graph.structure.concurrent.ConcurrentEdge;
import nari.graph.structure.concurrent.ConcurrentNode;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持同步的网络构造器
 * @author birderyu
 * @date 2017/4/22
 */
public class ConcurrentGraphBuilder
	implements GraphBuilder {

    protected Graph graph;
    private Map<Node, Boolean> nodes;
    private Map<Edge, Boolean> edges;

    public ConcurrentGraphBuilder(Graph graph) {
        nodes = new ConcurrentHashMap<Node, Boolean>();
        edges = new ConcurrentHashMap<Edge, Boolean>();
        this.graph = graph;
    }
    
    @Override
	public void clear() {
    	nodes.clear();
    	edges.clear();
    }
    
    @Override
	public Graph getGraph() {
    	graph.setNodes(nodes.keySet());
    	graph.setEdges(edges.keySet());
    	return graph;
	}
    
    @Override
	public void addEdge(Edge edge) {
		edge.getNodeA().add(edge);
		if (!edge.getNodeA().equals(edge.getNodeB())) {
			edge.getNodeB().add(edge);
		}
		edges.put(edge, Boolean.TRUE);
	}

	@Override
	public void addNode(Node node) {
		nodes.put(node, Boolean.FALSE);
	}

	@Override
	public Edge buildEdge(Node nodeA, Node nodeB) {
		Edge edge = new ConcurrentEdge(graph.createComponentId(), nodeA, nodeB);
		nodeA.add(edge);
    	nodeB.add(edge);
    	return edge;
	}

	@Override
	public Node buildNode() {
		return new ConcurrentNode(graph.createComponentId());
	}

	@Override
	public void removeEdge(Edge edge) {
		edge.getNodeA().remove(edge);
		edge.getNodeB().remove(edge);
		edges.remove(edge);
	}

	@Override
	public void removeEdges(Iterator<Edge> edges) {
		while (edges.hasNext()) {
			removeEdge(edges.next());
		}
	}

	@Override
	public void removeNode(Node node) {
		removeEdges(node.getEdges().iterator());
		this.nodes.remove(node);
	}

	@Override
	public void removeNodes(Iterator<Node> nodes) {
		while (nodes.hasNext()) {
			removeNode(nodes.next());
		}
	}
}
