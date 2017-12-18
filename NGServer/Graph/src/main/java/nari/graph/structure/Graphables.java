package nari.graph.structure;

import java.util.Iterator;

/**
 * 网络单元的集合
 * 
 * @author birderyu
 *
 */
public interface Graphables {

	int size();
	
	boolean isEmpty();
	
	/**
	 * 添加一个拓扑单元
	 * 
	 * @param component
	 */
	void add(Graphable component);
	
	/**
	 * 移除一个拓扑单元
	 * 
	 * @param component
	 */
	void remove(Graphable component);
	
	/**
	 * 获取拓扑单元的迭代器
	 * 
	 * @return
	 */
	Iterator<Graphable> iterator();
}
