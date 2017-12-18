package nari.graph.traverse.simple;

import nari.graph.structure.Graph;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.GraphWalker;
import nari.graph.traverse.basic.AbstractGraphTraversal;

public class SimpleGraphTraversal 
	extends AbstractGraphTraversal {
	
	public SimpleGraphTraversal(Graph network, GraphWalker walker, GraphIterator iterator) {
		super(network, walker, iterator);
	}
	
	@Override
	public GraphTracker getTracker() {
		return new SimpleGraphTracker(getNetwork(), getWalker(), getIterator());
	}
}
