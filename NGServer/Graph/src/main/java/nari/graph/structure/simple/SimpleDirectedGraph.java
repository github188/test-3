package nari.graph.structure.simple;

import java.util.List;

import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.structure.basic.AbstractDirectedGraph;

public class SimpleDirectedGraph 
	extends AbstractDirectedGraph {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8179222653944763173L;
	
	/**
     * ID分配器
     */
	private int currentId = 0;
	
	@Override
	public int createComponentId() {
		return currentId++;
	}
	
	@Override
	public int size() {
		return currentId;
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
