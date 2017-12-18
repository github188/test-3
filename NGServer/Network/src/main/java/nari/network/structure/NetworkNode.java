package nari.network.structure;

import java.util.Iterator;

import nari.graph.structure.Node;
import nari.network.device.TopoDevice;

/**
 * 拓扑节点为一个具体的设备
 * 
 * @author birderyu
 *
 */
public interface NetworkNode
	extends Node {

	void setDevice(TopoDevice device);
	
	TopoDevice getDevice();
	
	Iterator<NetworkNode> getDirectedRelated(int terminal);
	
	Iterator<NetworkNode> getDirectedRelated(int[] terminals);
}
