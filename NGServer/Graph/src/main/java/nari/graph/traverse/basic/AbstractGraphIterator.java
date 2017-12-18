package nari.graph.traverse.basic;

import java.util.Deque;
import java.util.LinkedList;

import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphTracker;

public abstract class AbstractGraphIterator
	implements GraphIterator {
	
	private Graphable source;
	
	/**
	 * 邻接集合
	 * 邻接集合表示即将访问的图元的集合，邻接集合中的第一个元素，就是即将访问到的下一个图元
	 */
	protected Deque<Graphable> nextSet;
	
	public AbstractGraphIterator() {
		nextSet = new LinkedList<Graphable>();
	}
	
	@Override
	public Graphable getSource() {
		return source;
	}
	
	@Override
	public void setSource(Graphable source) {
		this.source = source;
	}

	@Override
	public void process(GraphTracker tracker, Graphable current) {
		process(tracker, current, current.getRelated());
	}
	
	@Override
	public void killBranch(GraphTracker tracker, Graphable current) {
		// do nothing
	}
}
