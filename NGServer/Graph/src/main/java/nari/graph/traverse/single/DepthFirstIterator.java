package nari.graph.traverse.single;

import java.util.*;

import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphPath;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.basic.AbstractGraphIterator;
import nari.graph.traverse.simple.SimpleGraphPath;

/**
 * 单源深度优先遍历
 * @author birderyu
 *
 */
public class DepthFirstIterator 
	extends AbstractGraphIterator {
	
	/**
	 * 存储当前的轨迹
	 * 当前的轨迹是一个栈
	 */
	private GraphPath currentPath;
	
	/**
	 * 是否开始一条新的轨迹
	 */
	private boolean startTrace = true;
	
	public DepthFirstIterator() {
		super();
		currentPath = new SimpleGraphPath();
		startTrace = true;
	}
	
	@Override
	public void initialize() {
		nextSet.clear();
		currentPath.clear();
		nextSet.addLast(getSource());
		startTrace = true;
	}

	@Override
	public Graphable next(GraphTracker tracker) {
		while (!nextSet.isEmpty()) {
			// 从邻接集合中找到一个未访问的图元
			Graphable next = nextSet.pollLast();
			if (!tracker.isVisited(next)) {
				if (startTrace) {
					// 开始一条新的轨迹，需要回滚当前的轨迹
					Graphable last = currentPath.top();
					while (null != last) {
						if (next.relateTo(last)) {
							// 若next与last相临接，则回滚结束
							break;
						}
						// 将要素弹出
						currentPath.pop();
						last = currentPath.top();
					}
				}
				return next;
			}
		}
		return null;
	}

	@Override
	public void process(GraphTracker tracker, Graphable current, Iterator<Graphable> nexts) {
		startTrace = true;
		while (nexts.hasNext()) {
			Graphable component = nexts.next();
			if (tracker.isVisited(component)) {
				continue;
			}
			nextSet.addLast(component);
			startTrace = false;
		}
	}
	
	@Override
	public void addToPath(Graphable component) {
		currentPath.push(component);
	}

	@Override
	public Graphable getLast(Graphable component) {
		return currentPath.top2();
	}

	@Override
	public GraphPath getPath(Graphable component) {
		return currentPath;
	}
}
