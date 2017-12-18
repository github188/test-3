package nari.graph.structure;

import java.util.Iterator;

public interface DirectedGraphable 
	extends Graphable {
	
	Iterator<Graphable> getInRelated();
	
	Iterator<Graphable> getOutRelated();
	
	boolean relateToIn(Graphable component);
	
	boolean relateToOut(Graphable component);

}
