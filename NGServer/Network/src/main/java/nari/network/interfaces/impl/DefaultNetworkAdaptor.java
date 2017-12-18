package nari.network.interfaces.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphPath;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.GraphTraversal;
import nari.graph.traverse.GraphWalker;
import nari.graph.traverse.concurrent.ConcurrentGraphTracker;
import nari.graph.traverse.concurrent.ConcurrentGraphTraversal;
import nari.graph.traverse.simple.SimpleGraphTracker;
import nari.graph.traverse.simple.SimpleGraphTraversal;
import nari.graph.traverse.single.BreadthFirstIterator;
import nari.graph.traverse.single.DepthFirstIterator;
import nari.network.device.TopoDevice;
import nari.network.interfaces.NetworkAdaptor;
import nari.network.structure.Network;
import nari.network.structure.NetworkNode;
import nari.network.structure.NetworkPath;
import nari.network.structure.NetworkVisitor;
import nari.network.structure.NetworkWalker;
import nari.network.structure.simple.SimpleNetworkPath;

public class DefaultNetworkAdaptor
	implements NetworkAdaptor {
	
	/**
	 * 全网
	 */
	private Network network = null;

	@Override
	public Network getNetwork() {
		return network;
	}
	
	@Override
	public void setNetwork(Network network) {
		this.network = network;
	}

	@Override
	public TopoDevice getDevice(int modelId, int oid) {
		return network.getDevice(modelId, oid);
	}

	@Override
	public Iterator<TopoDevice> getRelated(TopoDevice device) {
		if (null == network || null == device) {
			return null;
		}
		NetworkNode node = network.getNode(device);
		if (null == node) {
			return null;
		}
		Iterator<Graphable> components = node.getRelated();
		if (null == components) {
			return null;
		}
		List<TopoDevice> relateds = new ArrayList<TopoDevice>();
		while (components.hasNext()) {
			Graphable component = components.next();
			if (!(component instanceof NetworkNode)) {
				continue;
			}
			relateds.add(((NetworkNode) component).getDevice());
		}
		return relateds.iterator();
	}
	
	public Iterator<TopoDevice> getDirectedRelated(TopoDevice device, int terminal) {
		if (null == network || null == device) {
			return null;
		}
		NetworkNode node = network.getNode(device);
		if (null == node) {
			return null;
		}
		Iterator<NetworkNode> nodes = node.getDirectedRelated(terminal);
		if (null == nodes) {
			return null;
		}
		List<TopoDevice> relateds = new ArrayList<TopoDevice>();
		while (nodes.hasNext()) {
			relateds.add(nodes.next().getDevice());
		}
		return relateds.iterator();
	}
	
	public Iterator<TopoDevice> getDirectedRelated(TopoDevice device, int[] terminals) {
		if (null == network || null == device) {
			return null;
		}
		NetworkNode node = network.getNode(device);
		if (null == node) {
			return null;
		}
		Iterator<NetworkNode> nodes = node.getDirectedRelated(terminals);
		if (null == nodes) {
			return null;
		}
		List<TopoDevice> relateds = new ArrayList<TopoDevice>();
		while (nodes.hasNext()) {
			relateds.add(nodes.next().getDevice());
		}
		return relateds.iterator();
	}

	@Override
	public void foreach(NetworkVisitor visitor) {
		
		if (null == visitor) {
			return;
		}
		
		final NetworkVisitor _visitor = visitor;
		network.visit(new GraphVisitor() {

			@Override
			public int visit(Graphable component) {
				
				if (!(component instanceof NetworkNode)) {
					return NetworkVisitor.PASS_AND_CONTINUE;
				}
				
				NetworkNode node = (NetworkNode) component;
				return _visitor.visit(node.getDevice());
				
			}
			
		});
	}

	@Override
	public boolean search(TopoDevice source, NetworkWalker walker, int searchMethod) {
		return search(source, walker, searchMethod, null);
	}
	
	@Override
	public boolean search(TopoDevice source,
						  NetworkWalker walker, int searchMethod, Map<TopoDevice, NetworkPath> traces) {
		
		if (null == network || null == source || null == walker) {
			return false;
		}
		
		GraphWalker graphWalker = getGraphWalker(network, walker, new ArrayList<TopoDevice>());
		GraphIterator graphIterator = getGraphIterator(searchMethod);		
		GraphTracker graphTracker = getGraphTracker(network, graphWalker, graphIterator);
		
		graphTracker.beforeTrack();
		if (traces == null) {
			return graphTracker.track(network.getNode(source));
		}
		Map<Graphable, GraphPath> graphTraces = new HashMap<Graphable, GraphPath>();
		boolean result = graphTracker.track(network.getNode(source), graphTraces);
		getNetworkTraces(graphTraces, traces);
		return result;
	}

	@Override
	public void search(TopoDevice[] sources, NetworkWalker walker, int searchMethod) {
		search(sources, walker, searchMethod, null);
	}

	@Override
	public void search(TopoDevice[] sources, NetworkWalker walker, int searchMethod, Map<TopoDevice, NetworkPath> traces) {
		
		if (null == network || null == sources || null == walker) {
			return;
		}
		
		GraphWalker graphWalker = getGraphWalker(network, walker, new ArrayList<TopoDevice>());
		GraphIterator graphIterator = getGraphIterator(searchMethod);		
		GraphTracker graphTracker = getGraphTracker(network, graphWalker, graphIterator);
		Graphable[] graphSources = getGraphSources(network, sources);
		
		// 开始进行追踪
		graphTracker.beforeTrack();
		if (traces == null) {
			graphTracker.track(graphSources);
		} else {
			Map<Graphable, GraphPath> graphTraces = new HashMap<Graphable, GraphPath>();
			graphTracker.track(graphSources, graphTraces);
			getNetworkTraces(graphTraces, traces);
		}
	}

	@Override
	public void traverse(NetworkWalker walker, int searchMethod) {
		traverse(walker, searchMethod, null);
	}
	
	@Override
	public void traverse(NetworkWalker walker, int searchMethod, Map<TopoDevice, NetworkPath> traces) {
		
		if (null == network || null == walker) {
			return;
		}
		
		GraphWalker graphWalker = getGraphWalker(network, walker, new ArrayList<TopoDevice>());
		GraphIterator graphIterator = getGraphIterator(searchMethod);
		GraphTraversal graphTraversal = getGraphTraversal(network, graphWalker, graphIterator);
		
		// 开始进行遍历
		if (traces == null) {
			graphTraversal.traverse();
		} else {
			Map<Graphable, GraphPath> graphTraces = new HashMap<Graphable, GraphPath>();
			graphTraversal.traverse(graphTraces);
			getNetworkTraces(graphTraces, traces);
		}
		
	}
	
	private GraphIterator getGraphIterator(int searchMethod) {
		if (DEPTH_FIRST == searchMethod) {
			return new DepthFirstIterator();
		}
		return new BreadthFirstIterator();
	}
	
	/**
	 * 构造回调函数
	 */
	private GraphWalker getGraphWalker(final Network network, final NetworkWalker walker, final List<TopoDevice> nextDevices) {

		return new GraphWalker() {
			@Override
			public int walk(Graphable last, Graphable current,
					List<Graphable> nexts) {
				
				if (!(current instanceof NetworkNode)) {
					return GraphWalker.PROCESS_EACH;
				}
				
				TopoDevice lastDevice = null;
				if (last instanceof NetworkNode) {
					lastDevice = ((NetworkNode) last).getDevice();
				}
				TopoDevice currentDevice = ((NetworkNode) current).getDevice();
				int status = walker.walk(lastDevice, currentDevice, nextDevices);
				if (status == GraphWalker.PROCESS_DIRECTED || 
						status == GraphWalker.PROCESS_DIRECTED_AND_COLLECT_TRACE) {
					// 若追踪是有方向的，将方向转换
					for (TopoDevice nextDevice : nextDevices) {
						nexts.add(network.getNode(nextDevice));
					}
					nextDevices.clear();
				}
				return status;
			}
		};
	}

	private GraphTracker getGraphTracker(Network network, GraphWalker graphWalker, GraphIterator graphIterator) {
		if (network.mulitThreadTrack()) {
			return new ConcurrentGraphTracker(network, graphWalker, graphIterator);
		}
		return new SimpleGraphTracker(network, graphWalker, graphIterator);
	}
	
	private GraphTraversal getGraphTraversal(Network network, GraphWalker graphWalker, GraphIterator graphIterator) {
		if (network.mulitThreadTrack()) {
			return new ConcurrentGraphTraversal(network, graphWalker, graphIterator);
		}
		return new SimpleGraphTraversal(network, graphWalker, graphIterator);
	}
	
	private void getNetworkTraces(Map<Graphable, GraphPath> graphTraces, Map<TopoDevice, NetworkPath> networkTraces) {
		
		if (null == graphTraces || null == networkTraces) {
			return;
		}
		
		for (Map.Entry<Graphable, GraphPath> graphEntry : graphTraces.entrySet()) {
			Graphable component = graphEntry.getKey();
			if (!(component instanceof NetworkNode)) {
				continue;
			}
			TopoDevice device = ((NetworkNode) component).getDevice();
			if (null == device) {
				continue;
			}
			networkTraces.put(device, new SimpleNetworkPath(graphEntry.getValue()));
		}
	}

	private Graphable[] getGraphSources(Network network, TopoDevice[] sources) {
		List<Graphable> components = new ArrayList<Graphable>(sources.length);
		for (TopoDevice device : sources) {
			NetworkNode node = network.getNode(device);
			if (null == node) {
				continue;
			}
			components.add(node);
		}
		return components.toArray(new Graphable[components.size()]);
	}
}
