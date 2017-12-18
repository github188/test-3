package nari.graph.structure.concurrent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.structure.basic.AbstractDirectedGraph;

public class ConcurrentDirectedGraph 
	extends AbstractDirectedGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7774449988503390490L;
	
	/**
     * ID分配器
     */
    private static AtomicInteger currentId = new AtomicInteger(0);

	@Override
	public int createComponentId() {
		return currentId.getAndIncrement();
	}
	
	@Override
	public int size() {
		return currentId.get();
	}

	@Override
	public List<Graphable> query(GraphVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void visit(GraphVisitor visitor) {
		// TODO Auto-generated method stub
		
	}
}
