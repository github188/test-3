package nari.graph.structure;

import java.util.List;

public interface DirectedNode
	extends Node, DirectedGraphable {
	
	void addIn(DirectedEdge edge);
	
	void addOut(DirectedEdge edge);
	
	void removeIn(DirectedEdge edge);
	
	void removeOut(DirectedEdge edge);
	
	DirectedEdge getInEdge(DirectedNode other);
	
	List<DirectedEdge> getInEdges(DirectedNode other);
	
	List<DirectedEdge> getInEdges();
	
	DirectedEdge getOutEdge(DirectedNode other);
	
	List<DirectedEdge> getOutEdges(DirectedNode other);
	
	List<DirectedEdge> getOutEdges();
	
	int getInDegree();
	
	int getOutDegree();

}
