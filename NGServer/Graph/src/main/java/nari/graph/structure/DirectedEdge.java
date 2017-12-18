package nari.graph.structure;

/**
 * 有向边
 * 
 * @author birderyu
 *
 */
public interface DirectedEdge 
	extends Edge, DirectedGraphable {
	
	DirectedNode getInNode();
	
	DirectedNode getOutNode();

}
