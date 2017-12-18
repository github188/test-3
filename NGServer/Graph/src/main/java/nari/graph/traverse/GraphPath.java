package nari.graph.traverse;

import java.util.Iterator;

import nari.graph.structure.Graphable;

public interface GraphPath {
	
	boolean isEmpty();
	
	int size();

	void push(Graphable component);
	
	Graphable pop();
	
	void append(GraphPath path);
	
	/**
	 * 栈顶
	 * 
	 * @return
	 */
	Graphable top();
	
	/**
	 * 栈顶下面的元素
	 * 
	 * @return
	 */
	Graphable top2();
	
	void clear();
	
	Iterator<Graphable> iterator();
}
