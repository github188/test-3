package nari.graph.traverse;

import java.util.Iterator;

import nari.graph.structure.Graphable;

/**
 * 网络迭代器
 * @author birderyu
 *
 */
public interface GraphIterator {
	
	Graphable getSource();
	
	void setSource(Graphable source);
	
	/**
	 * 
	 */
	void initialize();
	
	Graphable next(GraphTracker tracker);
	
	void process(GraphTracker tracker, Graphable current);
	
	void process(GraphTracker tracker, Graphable current, Iterator<Graphable> nexts);
	
	void killBranch(GraphTracker tracker, Graphable current);
	
	void addToPath(Graphable component);
	
	Graphable getLast(Graphable component);
	
	GraphPath getPath(Graphable component);
}
