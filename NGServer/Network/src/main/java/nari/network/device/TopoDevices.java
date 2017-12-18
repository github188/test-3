package nari.network.device;

import java.util.Iterator;

public interface TopoDevices {
	
	boolean isEmpty();
	
	int size();

	void add(TopoDevice device);
	
	void remove(TopoDevice device);
	
	Iterator<TopoDevice> iterator();
}
