package nari.graph.traverse;

import java.util.Map;

import nari.graph.structure.Graph;
import nari.graph.structure.Graphable;

/**
 * 网络遍历器
 * 
 * @author birderyu
 *
 */
public interface GraphTraversal {
	
	Graph getNetwork();

	GraphWalker getWalker();
	
	GraphIterator getIterator();
	
	GraphTracker getTracker();
	
	void setNetwork(Graph network);
	
	void setWalker(GraphWalker walker);
	
	void setIterator(GraphIterator iterator);

	/**
	 * 遍历
	 */
	void traverse();
	
	/**
	 * 遍历并收集轨迹
	 */
	void traverse(Map<Graphable, GraphPath> traces);
}
