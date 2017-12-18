package nari.graph.structure.simple;

import nari.graph.structure.DirectedEdge;
import nari.graph.structure.DirectedNode;
import nari.graph.structure.basic.AbstractGraphable;

public abstract class SimpleDirectedEdge 
	extends AbstractGraphable implements DirectedEdge {
	
	private DirectedNode inNode;
	
	private DirectedNode outNode;

	public SimpleDirectedEdge(DirectedNode inNode,
			DirectedNode outNode) {
		super();
		this.inNode = inNode;
		this.outNode = outNode;
	}
	
	public DirectedNode getInNode() {
		return inNode;
	}
	
	public DirectedNode getOutNode() {
		return outNode;
	}

}
