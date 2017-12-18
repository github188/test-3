package nari.network.structure;

import java.util.Iterator;

import nari.graph.traverse.GraphPath;
import nari.network.device.TopoDevice;

public interface NetworkPath {
	
	boolean isEmpty();
	
	int size();
	
	void clear();
	
	void push(TopoDevice device);
	
	TopoDevice pop();
	
	void append(NetworkPath networkPath);
	
	void append(GraphPath graphPath);
	
	Iterator<TopoDevice> iterator();
}
