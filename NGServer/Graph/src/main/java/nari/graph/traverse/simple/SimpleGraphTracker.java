package nari.graph.traverse.simple;

import nari.graph.structure.Graph;
import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.structure.simple.SimpleGraphable;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphWalker;
import nari.graph.traverse.basic.AbstractGraphTracker;

public class SimpleGraphTracker 
	extends AbstractGraphTracker{

	public SimpleGraphTracker(Graph network, GraphWalker walker, GraphIterator iterator) {
		super(network, walker, iterator);
	}

	@Override
	public void setVisited(Graphable component, boolean visited) {
		((SimpleGraphable) component).setVisited(visited);
	}

	@Override
	public boolean isVisited(Graphable component) {
		return ((SimpleGraphable) component).isVisited();
	}

	@Override
	public void beforeTrack() {
		
		GraphVisitor visitor = new GraphVisitor() {
			public int visit(Graphable component) {
				SimpleGraphable c = (SimpleGraphable) component;
				c.setVisited(false);
				c.setCount(0);
				return GraphVisitor.PASS_AND_CONTINUE;
			}
		};
		
		// 初始化，将edge和node全部置为未访问状态
		getGraph().visit(visitor);
	}

}
