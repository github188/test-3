package nari.network.creator.concurrent;

import java.util.concurrent.ConcurrentHashMap;

import nari.graph.structure.Graphables;
import nari.graph.structure.concurrent.ConcurrentGraphables;
import nari.network.creator.NetworkGenerator;
import nari.network.device.TopoDevice;
import nari.network.device.TopoDevices;
import nari.network.device.concurrent.ConcurrentTopoDeviceSet;
import nari.network.device.concurrent.ConcurrentTopoDevices;
import nari.network.structure.NetworkNode;
import nari.network.structure.concurrent.ConcurrentNetwork;

public class ConcurrentNetworkGenerator
	extends NetworkGenerator {
	
	public ConcurrentNetworkGenerator() {
		connectionNodes = new ConcurrentHashMap<Long, TopoDevices>();
		nodeMap = new ConcurrentHashMap<TopoDevice, NetworkNode>();
		edgeMap = new ConcurrentHashMap<Long, Graphables>();
		devices = new ConcurrentTopoDeviceSet();
		network = new ConcurrentNetwork(devices, nodeMap);
		builder = new ConcurrentNetworkBuilder(network);
	}

	@Override
	protected Graphables buildNetworkables() {
		return new ConcurrentGraphables();
	}

	@Override
	protected TopoDevices buildDevices() {
		return new ConcurrentTopoDevices();
	}
}
