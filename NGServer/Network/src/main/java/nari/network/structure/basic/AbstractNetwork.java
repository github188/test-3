package nari.network.structure.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.structure.Node;
import nari.graph.structure.basic.AbstractGraph;
import nari.network.device.TopoDevice;
import nari.network.device.TopoDeviceSet;
import nari.network.structure.Network;
import nari.network.structure.NetworkNode;

public abstract class AbstractNetwork
	extends AbstractGraph implements Network {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6213949357980508676L;

	/**
	 * 拓扑设备的集合
	 */
	private TopoDeviceSet devices;
	
	/**
	 * <拓扑设备, 网络节点>
	 */
	private Map<TopoDevice, NetworkNode> nodeMap;
	
	public AbstractNetwork(TopoDeviceSet devices,
						   Map<TopoDevice, NetworkNode> nodeMap) {
		this.devices = devices;
		this.nodeMap = nodeMap;
	}
	
	@Override
	public List<Graphable> query(GraphVisitor visitor) {
		Set<Node> nodes = getNodes();
		List<Graphable> result = new ArrayList<Graphable>();
		for (Iterator<Node> iter = nodes.iterator(); iter.hasNext(); ) {
			Node edge = iter.next();
			switch (visitor.visit(edge)) {
			case GraphVisitor.PASS_AND_CONTINUE:
				result.add(edge);
				continue;
			case GraphVisitor.PASS_AND_STOP:
				result.add(edge);
				return result;
			case GraphVisitor.FAIL_QUERY:
				continue;
			}
		}
		return result;
	}
	
	@Override
	public void visit(GraphVisitor visitor) {
		visitNodes(visitor);
	}
	
	@Override
	public TopoDevice getDevice(int modelId, int oid) {
		return devices.get(modelId, oid);
	}
	
	@Override
	public NetworkNode getNode(TopoDevice device) {
		return nodeMap.get(device);
	}
}
