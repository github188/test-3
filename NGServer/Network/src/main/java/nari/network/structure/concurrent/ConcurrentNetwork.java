package nari.network.structure.concurrent;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import nari.network.device.TopoDevice;
import nari.network.device.TopoDeviceSet;
import nari.network.structure.NetworkNode;
import nari.network.structure.basic.AbstractNetwork;

public class ConcurrentNetwork
	extends AbstractNetwork {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170148749448697501L;
	
	/**
     * ID分配器
     */
    private static AtomicInteger currentId = new AtomicInteger(0);

	public ConcurrentNetwork(TopoDeviceSet devices,
                             Map<TopoDevice, NetworkNode> nodeMap) {
		super(devices, nodeMap);
	}

	@Override
	public int createComponentId() {
		return currentId.getAndIncrement();
	}
	
	@Override
	public boolean mulitThreadTrack() {
		return true;
	}
}
