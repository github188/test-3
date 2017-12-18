package nari.network.structure.simple;

import java.util.Map;

import nari.network.device.TopoDevice;
import nari.network.device.TopoDeviceSet;
import nari.network.structure.NetworkNode;
import nari.network.structure.basic.AbstractNetwork;

public class SimpleNetwork
	extends AbstractNetwork {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3380320350748616910L;
	
	/**
     * ID分配器
     */
	private int currentId = 0;

	public SimpleNetwork(TopoDeviceSet devices,
                         Map<TopoDevice, NetworkNode> nodeMap) {
		super(devices, nodeMap);
	}

	@Override
	public int createComponentId() {
		return currentId++;
	}
	
	@Override
	public boolean mulitThreadTrack() {
		return false;
	}
}
