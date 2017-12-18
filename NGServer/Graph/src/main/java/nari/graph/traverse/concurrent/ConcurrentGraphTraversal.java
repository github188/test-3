package nari.graph.traverse.concurrent;

import nari.graph.structure.Graph;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.GraphWalker;
import nari.graph.traverse.basic.AbstractGraphTraversal;

public class ConcurrentGraphTraversal 
	extends AbstractGraphTraversal {
	
	public ConcurrentGraphTraversal(Graph network, GraphWalker walker, GraphIterator iterator) {
		super(network, walker, iterator);
	}
	
	@Override
	public GraphTracker getTracker() {
		return new ConcurrentGraphTracker(getNetwork(), getWalker(), getIterator());
	}
}
