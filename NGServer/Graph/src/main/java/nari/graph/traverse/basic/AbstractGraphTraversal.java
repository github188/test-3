package nari.graph.traverse.basic;

import java.util.Map;

import nari.graph.structure.Graph;
import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphPath;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.GraphTraversal;
import nari.graph.traverse.GraphWalker;

public abstract class AbstractGraphTraversal 
	implements GraphTraversal {
	
	private Graph network;
	private GraphWalker walker;
	private GraphIterator iterator;
	
	public AbstractGraphTraversal(Graph network, GraphWalker walker, GraphIterator iterator) {
		this.network = network;
		this.walker = walker;
		this.iterator = iterator;
	}
	
	@Override
	public Graph getNetwork() {
		return network;
	}
	
	@Override
	public GraphWalker getWalker() {
		return walker;
	}

	@Override
	public GraphIterator getIterator() {
		return iterator;
	}
	
	@Override
	public void setNetwork(Graph network) {
		this.network = network;
	}

	@Override
	public void setWalker(GraphWalker walker) {
		this.walker = walker;
	}
	
	@Override
	public void setIterator(GraphIterator iterator) {
		this.iterator = iterator;
	}

	@Override
	public void traverse() {
		traverse(null);
	}
	
	@Override
	public void traverse(Map<Graphable, GraphPath> traces) {
		
		final Map<Graphable, GraphPath> _traces = traces;
		final GraphTracker tracker = getTracker();
		tracker.beforeTrack();
		getNetwork().visit(new GraphVisitor() {
			public int visit(Graphable component) {
				if (!tracker.isVisited(component)) {
					// 若当前单元还未被访问过，则以该单元作为起点，进行追踪
					tracker.track(component, _traces);
				}
				return GraphVisitor.PASS_AND_CONTINUE;
			}
		});
		
	}

}
