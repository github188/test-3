package nari.graph.traverse.concurrent;

import java.util.HashMap;
import java.util.Map;

import nari.graph.structure.Graph;
import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphWalker;
import nari.graph.traverse.basic.AbstractGraphTracker;

public class ConcurrentGraphTracker 
	extends AbstractGraphTracker {
	
	/**
	 * 判断当前元素是否已经被访问过
	 */
	private Map<Integer, Boolean> visited = null;

	public ConcurrentGraphTracker(Graph network, GraphWalker walker, GraphIterator iterator) {
		super(network, walker, iterator);
		visited = new HashMap<Integer, Boolean>();
	}

	@Override
	public void setVisited(Graphable component, boolean visited) {
		if (visited) {
			this.visited.put(component.getId(), Boolean.TRUE);
		} else {
			this.visited.put(component.getId(), Boolean.FALSE);
		}
	}

	@Override
	public boolean isVisited(Graphable component) {
		return Boolean.TRUE == visited.get(component.getId());
	}

	@Override
	public void beforeTrack() {
		visited.clear();
	}
}
