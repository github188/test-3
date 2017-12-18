package nari.network.structure;

import nari.graph.structure.Graph;
import nari.network.device.TopoDevice;

/**
 * 拓扑网络
 * 
 * @author birderyu
 *
 */
public interface Network
	extends Graph {
	
	TopoDevice getDevice(int modelId, int oid);
	
	NetworkNode getNode(TopoDevice device);
	
	/**
	 * 是否支持多线程查询
	 * 
	 * @return
	 */
	boolean mulitThreadTrack();
}
