package nari.graph.traverse;

import java.util.Map;

import nari.graph.structure.Graph;
import nari.graph.structure.Graphable;

/**
 * 网络追踪者
 * 网络追踪者从一个或多个网络单元出发，按照一定的方向进行追踪，并将当前追踪的轨迹保留下来
 * 
 * @author birderyu
 *
 */
public interface GraphTracker {
	
	Graph getGraph();

	GraphWalker getWalker();
	
	GraphIterator getIterator();
	
	void setGraph(Graph graph);
	
	void setWalker(GraphWalker walker);
	
	void setIterator(GraphIterator iterator);
	
	void setVisited(Graphable component, boolean visited);
	
	boolean isVisited(Graphable component);
	
	/**
	 * 追踪之前调用此方法
	 */
	void beforeTrack();
	
	/**
	 * 单源追踪
	 * 
	 * @param source
	 * @return
	 */
	boolean track(Graphable source);

	/**
	 * 单源追踪并收集轨迹
	 * 
	 * @param source
	 * @param traces
	 * @return
	 */
	boolean track(Graphable source, Map<Graphable, GraphPath> traces);
	
	/**
	 * 多源追踪
	 * 
	 * @param sources
	 */
	void track(Graphable[] sources);
	
	/**
	 * 多源追踪并收集轨迹
	 * 
	 * @param sources
	 * @param trace
	 */
	void track(Graphable[] sources, Map<Graphable, GraphPath> traces);
}
