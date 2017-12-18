package nari.network.device;

import java.util.Iterator;

/**
 * 拓扑设备集合
 */
public interface TopoDeviceSet {
	
	boolean isEmpty();
	
	int size();
	
	void add(TopoDevice device);
	
	void remove(TopoDevice device);
	
	void remove(int modelId, int oid);
	
	TopoDevice get(int modelId, int oid);
	
	Iterator<TopoDevice> get(int modelId);
}
