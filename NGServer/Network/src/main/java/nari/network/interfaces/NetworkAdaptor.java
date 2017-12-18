package nari.network.interfaces;

import java.util.Iterator;
import java.util.Map;

import nari.network.device.TopoDevice;
import nari.network.structure.Network;
import nari.network.structure.NetworkPath;
import nari.network.structure.NetworkVisitor;
import nari.network.structure.NetworkWalker;

/**
 * 拓扑服务
 * 
 * @author birderyu
 *
 */
public interface NetworkAdaptor {
	
	NetworkAdaptor NONE = new NetworkAdaptor() {

		@Override
		public Network getNetwork () {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public void setNetwork(Network network) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public TopoDevice getDevice(int modelId, int oid) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public Iterator<TopoDevice> getRelated(TopoDevice device) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public Iterator<TopoDevice> getDirectedRelated(TopoDevice device, int terminal) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public Iterator<TopoDevice> getDirectedRelated(TopoDevice device, int[] terminals) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public void foreach(NetworkVisitor visitor) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public boolean search(TopoDevice source, NetworkWalker walker, int searchMethod) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public boolean search(TopoDevice source,
							  NetworkWalker walker, int searchMethod,
							  Map<TopoDevice, NetworkPath> traces) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public void search(TopoDevice[] sources,
						   NetworkWalker walker, int searchMethod) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public void search(TopoDevice[] sources,
						   NetworkWalker walker, int searchMethod,
						   Map<TopoDevice, NetworkPath> traces) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public void traverse(NetworkWalker walker,
                             int searchMethod) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

		@Override
		public void traverse(NetworkWalker walker,
                             int searchMethod, Map<TopoDevice, NetworkPath> traces) {
			throw new UnsupportedOperationException("not implemented this method.");
		}

	};
	
	/**
	 * 广度优先
	 */
	int BREADTH_FIRST = 0;
	
	/**
	 * 深度优先
	 */
	int DEPTH_FIRST = 1;

	/**
	 * 获取全网拓扑
	 * 
	 * @return
	 */
	Network getNetwork();
	
	/**
	 * 设置全网拓扑
	 * 
	 * @param network
	 */
	void setNetwork(Network network);

	/**
	 *
	 *
	 * @param modelId
	 * @param oid
	 * @return
	 */
	TopoDevice getDevice(int modelId, int oid);
	
	/**
	 * 获取与当前设备相邻接的所有设备
	 *
	 * @param device 设备
	 * @return
	 */
	Iterator<TopoDevice> getRelated(TopoDevice device);
	
	/**
	 * 获取与当前设备在某一个方向上相邻接的所有设备
	 *
	 * @param device 设备
	 * @param terminal 端子序号，如0表示0号端子
	 * @return
	 */
	Iterator<TopoDevice> getDirectedRelated(TopoDevice device, int terminal);
	
	/**
	 * 获取与当前设备在某几个方向上相邻接的所有设备
	 *
	 * @param device 设备
	 * @param terminals 端子序号，如[0,2]表示0号端子与2号端子
	 * @return
	 */
	Iterator<TopoDevice> getDirectedRelated(TopoDevice device, int[] terminals);
		
	/**
	 * 迭代访问网络中所有的设备
	 *
	 * @param visitor 访问回调
	 */
	void foreach(NetworkVisitor visitor);
	
	/**
	 * 单源搜索算法
	 * 从一个设备作为起点进行网络追踪，返回追踪的结果
	 *
	 * @param source 源点设备
	 * @param walker 追踪回调
	 * @param searchMethod 搜索方法
	 * @return
	 */
	boolean search(TopoDevice source, NetworkWalker walker, int searchMethod);
	
	/**
	 * 单源搜索算法，并收集轨迹
	 *
	 * @param source 源点设备
	 * @param walker 追踪回调
	 * @param searchMethod 搜索方法
	 * @param traces 收集上来的轨迹，可以为null
	 * @return
	 */
	boolean search(TopoDevice source, NetworkWalker walker, int searchMethod, Map<TopoDevice, NetworkPath> traces);
	
	/**
	 * 多源搜索算法
	 *
	 * @param sources 源点设备
	 * @param walker 追踪回调
	 * @param searchMethod 搜索方法
	 */
	void search(TopoDevice[] sources, NetworkWalker walker, int searchMethod);
	
	/**
	 * 多源搜索算法，并收集轨迹
	 *
	 * @param sources 源点设备
	 * @param walker 追踪回调
	 * @param searchMethod 搜索方法
	 * @param traces 收集上来的轨迹，可以为null
	 */
	void search(TopoDevice[] sources, NetworkWalker walker, int searchMethod, Map<TopoDevice, NetworkPath> traces);

	/**
	 *
	 *
	 * @param walker
	 * @param searchMethod
	 */
	void traverse(NetworkWalker walker, int searchMethod);

	/**
	 *
	 *
	 * @param walker
	 * @param searchMethod
	 * @param traces
	 */
	void traverse(NetworkWalker walker, int searchMethod, Map<TopoDevice, NetworkPath> traces);
}
