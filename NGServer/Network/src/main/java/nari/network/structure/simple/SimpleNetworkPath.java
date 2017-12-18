package nari.network.structure.simple;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphPath;
import nari.network.device.TopoDevice;
import nari.network.structure.NetworkNode;
import nari.network.structure.NetworkPath;

public class SimpleNetworkPath
	implements NetworkPath {
	
	private Deque<TopoDevice> devices;
	
	public SimpleNetworkPath() {
		devices = new LinkedList<TopoDevice>();
	}
	
	public SimpleNetworkPath(TopoDevice device) {
		devices = new LinkedList<TopoDevice>();
		push(device);
	}
	
	public SimpleNetworkPath(NetworkPath networkPath) {
		devices = new LinkedList<TopoDevice>();
		append(networkPath);
	}
	
	public SimpleNetworkPath(GraphPath graphPath) {
		devices = new LinkedList<TopoDevice>();
		append(graphPath);
	}

	@Override
	public boolean isEmpty() {
		return devices.isEmpty();
	}

	@Override
	public int size() {
		return devices.size();
	}

	@Override
	public void clear() {
		devices.clear();
	}

	@Override
	public void push(TopoDevice device) {
		devices.addLast(device);
	}

	@Override
	public TopoDevice pop() {
		return devices.pollLast();
	}
	
	@Override
	public void append(NetworkPath networkPath) {
		Iterator<TopoDevice> iter = networkPath.iterator();
		while (iter.hasNext()) {
			push(iter.next());
		}
	}
	
	@Override
	public void append(GraphPath graphPath) {
		Iterator<Graphable> iter = graphPath.iterator();
		while (iter.hasNext()) {
			Graphable component = iter.next();
			if (!(component instanceof NetworkNode)) {
				continue;
			}
			TopoDevice device = ((NetworkNode) component).getDevice();
			if (null == device) {
				continue;
			}
			push(device);
		}
	}

	@Override
	public Iterator<TopoDevice> iterator() {
		return devices.iterator();
	}

}
