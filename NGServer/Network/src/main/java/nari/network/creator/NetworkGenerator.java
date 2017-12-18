package nari.network.creator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.graph.build.GraphBuilder;
import nari.graph.build.GraphGenerator;
import nari.graph.structure.Graph;
import nari.graph.structure.Graphable;
import nari.graph.structure.Graphables;
import nari.network.creator.concurrent.ConcurrentNetworkGenerator;
import nari.network.creator.simple.SimpleNetworkGenerator;
import nari.network.device.TopoDevice;
import nari.network.device.TopoDeviceSet;
import nari.network.device.TopoDevices;
import nari.network.structure.NetworkEdge;
import nari.network.structure.Network;
import nari.network.structure.NetworkNode;

public abstract class NetworkGenerator
	implements GraphGenerator {
	
	/**
	 * <NodeId, 拓扑设备>
	 */
	protected Map<Long, TopoDevices> connectionNodes;
	
	/**
	 * <拓扑设备, 网络节点>
	 */
	protected Map<TopoDevice, NetworkNode> nodeMap;
	
	/**
	 * <NodeId, 网络边的集合>
	 */
	protected Map<Long, Graphables> edgeMap;
	
	/**
	 * 拓扑设备的集合
	 */
	protected TopoDeviceSet devices;
	
	/**
	 * 拓扑网络
	 */
	protected Network network;
	
	/**
	 * 网络构造器
	 */
	protected NetworkBuilder builder;
	
	static public NetworkGenerator getTopoNetworkGenerator(int threadNum) {
		if (threadNum > 1) {
			return new ConcurrentNetworkGenerator();
		}
		return new SimpleNetworkGenerator();
	}

	@Override
	public Graphable add(Object obj) {
		if (obj instanceof TopoDevice) {
			TopoDevice device = (TopoDevice) obj;
			devices.add(device);
			long[] connectionNodes = device.getConnectionNodes();
			if (null == connectionNodes) {
				return null;
			}
			for (long connectionNode : connectionNodes) {
				TopoDevices _devices = this.connectionNodes.get(connectionNode);
				if (_devices == null) {
					_devices = buildDevices();
					this.connectionNodes.put(connectionNode, _devices);
				}
				_devices.add(device);
			}
			
			NetworkNode node = builder.buildNetworkNode(device);
			builder.addNode(node);
			nodeMap.put(device, node);
			return node;
		}
		return null;
	}
	
	public Graph buildGraph() {
		for (Map.Entry<Long, TopoDevices> entry : connectionNodes.entrySet()) {
			buildByNodeId(entry.getKey(), entry.getValue().iterator());
		}
		return builder.getGraph();
	}

	@Override
	public Graph getGraph() {
		return builder.getGraph();
	}

	@Override
	public GraphBuilder getGraphBuilder() {
		return builder;
	}

	@Override
	public Graphable remove(Object obj) {
		// TODO
		return null;
	}

	@Override
	public void setGraphBuilder(GraphBuilder builder) {
		this.builder = (NetworkBuilder) builder;
	}
	
	private void buildByNodeId(long connectionNode, Iterator<TopoDevice> devices) {
		
		if (null == devices) {
			return;
		}
		
		NetworkNode busNode = null;
		List<NetworkNode> nodes = new ArrayList<NetworkNode>();
		int nodeSize = 0;
		while (devices.hasNext()) {
			TopoDevice device = devices.next();
			NetworkNode node = this.nodeMap.get(device);
			if (null == busNode && device.isBus()) {
				busNode = node;
			} else {
				nodes.add(node);
			}
			nodeSize++;
		}
		
		if (nodeSize <= 0) {
			return;
		}
		
		Graphables networkables = buildNetworkables();
		if (null == busNode) {
			// 没有母线
			for (int i = 0; i < nodes.size(); i++) {
				for (int j = i + 1; j < nodes.size(); j++) {
					NetworkNode nodeA = nodes.get(i);
					NetworkNode nodeB = nodes.get(j);
					
					NetworkEdge edge = builder.buildNetworkEdge(nodeA, nodeB, connectionNode);
					builder.addEdge(edge);
					networkables.add(edge);
				}
			}
		} else {
			// 有母线
			for (NetworkNode node : nodes) {
				NetworkEdge edge = builder.buildNetworkEdge(busNode, node, connectionNode);
				builder.addEdge(edge);
				networkables.add(edge);
			}
		}
		edgeMap.put(connectionNode, networkables);
	}
	
	abstract protected Graphables buildNetworkables();
	
	abstract protected TopoDevices buildDevices();
}
