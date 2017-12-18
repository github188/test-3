package nari.graph.build;

import nari.graph.structure.Graph;
import nari.graph.structure.Graphable;

/**
 * 网络创建者
 * 
 * @author birderyu
 *
 */
public interface GraphGenerator {
	
	Graphable add(Object obj);
	
	Graphable remove(Object obj);

	void setGraphBuilder(GraphBuilder builder);
	
	GraphBuilder getGraphBuilder();
	
	Graph getGraph();
	
}
