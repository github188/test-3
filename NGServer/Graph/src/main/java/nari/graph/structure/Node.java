package nari.graph.structure;

import java.util.List;

/**
 * 网络节点
 * @author birderyu
 *
 */
public interface Node 
	extends Graphable {

	void add(Edge edge);
	
	void remove(Edge edge);

	Edge getEdge(Node node);

	List<Edge> getEdges(Node node);

	List<Edge> getEdges();
	
	/**
	 * 获取节点的度
	 * @return
	 */
	int getDegree();
	
}
