package nari.network.structure;

import nari.graph.structure.Edge;
import nari.network.bean.NetworkConnection;

/**
 * 拓扑边表示一个连接关系
 * 
 * @author birderyu
 *
 */
public interface NetworkEdge
	extends Edge {

	long getConnectionNode();
	
	void setConnectionNode(long connectionNode);
	
	NetworkConnection getNetworkConnection();
}
