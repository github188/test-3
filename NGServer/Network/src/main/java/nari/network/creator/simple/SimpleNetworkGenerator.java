package nari.network.creator.simple;

import java.util.HashMap;

import nari.graph.structure.Graphables;
import nari.graph.structure.simple.SimpleGraphables;
import nari.network.creator.NetworkGenerator;
import nari.network.device.TopoDevice;
import nari.network.device.TopoDevices;
import nari.network.device.simple.SimpleTopoDeviceSet;
import nari.network.device.simple.SimpleTopoDevices;
import nari.network.structure.NetworkNode;
import nari.network.structure.simple.SimpleNetwork;

public class SimpleNetworkGenerator
	extends NetworkGenerator {
	
	public SimpleNetworkGenerator() {
		connectionNodes = new HashMap<Long, TopoDevices>();
		nodeMap = new HashMap<TopoDevice, NetworkNode>();
		edgeMap = new HashMap<Long, Graphables>();
		devices = new SimpleTopoDeviceSet();
		network = new SimpleNetwork(devices, nodeMap);
		builder = new SimpleNetworkBuilder(network);
	}

	@Override
	protected Graphables buildNetworkables() {
		return new SimpleGraphables();
	}

	@Override
	protected TopoDevices buildDevices() {
		return new SimpleTopoDevices();
	}

}
