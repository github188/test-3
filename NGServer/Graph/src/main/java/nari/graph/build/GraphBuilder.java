package nari.graph.build;

import java.util.Iterator;

import nari.graph.structure.Edge;
import nari.graph.structure.Graph;
import nari.graph.structure.Node;

 /**
  * 网络构建器
  * @author birderyu
  *
  */
public interface GraphBuilder {
	
	void clear();
	
	Graph getGraph();
	
	void addEdge(Edge edge);

	void addNode(Node node);

	Edge buildEdge(Node nodeA, Node nodeB);

	Node buildNode();
	
	void removeEdge(Edge edge);

	void removeEdges(Iterator<Edge> edges);
	
	void removeNode(Node node);

	void removeNodes(Iterator<Node> nodes);
}
