package nari.graph.build.simple;

import nari.graph.build.GraphBuilder;
import nari.graph.structure.Edge;
import nari.graph.structure.Graph;
import nari.graph.structure.Node;
import nari.graph.structure.simple.SimpleEdge;
import nari.graph.structure.simple.SimpleNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 一个简单的网络构建器
 * 该构建器用于在单线程环境中构建拓扑网络
 *
 * @author birderyu
 * @date 2017/4/22
 */
public class SimpleGraphBuilder
	implements GraphBuilder {

	protected Graph graph;
    private Set<Node> nodes;
    private Set<Edge> edges;

    public SimpleGraphBuilder(Graph graph) {
        nodes = new HashSet<Node>();
        edges = new HashSet<Edge>();
        this.graph = graph;
    }
    
    @Override
	public void clear() {
    	nodes.clear();
    	edges.clear();
    }
    
    @Override
	public Graph getGraph() {
    	graph.setNodes(nodes);
    	graph.setEdges(edges);
    	return graph;
	}
    
    @Override
    public void addEdge(Edge edge) {
        edge.getNodeA().add(edge);
        if (!edge.getNodeA().equals(edge.getNodeB())) {
            edge.getNodeB().add(edge);
        }
        edges.add(edge);
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
    }
    
    @Override
	public Edge buildEdge(Node nodeA, Node nodeB) {
    	Edge edge = new SimpleEdge(graph.createComponentId(), nodeA, nodeB);
    	nodeA.add(edge);
    	nodeB.add(edge);
    	return edge;
	}

	@Override
	public Node buildNode() {
		return new SimpleNode(graph.createComponentId());
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
