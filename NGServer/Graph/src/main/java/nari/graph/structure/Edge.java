package nari.graph.structure;

/**
 * 网络节点
 * @author birderyu
 *
 */
public interface Edge 
	extends Graphable {
	
	/**
	 * 同边且同向
	 */
	public static final int EQUAL_NODE_ORIENTATION = 0;
	
	/**
	 * 不同边
	 */
	public static final int UNEQUAL_NODE_ORIENTATION = 1;
	
	/**
	 * 同边且反向
	 */
	public static final int OPPOSITE_NODE_ORIENTATION = -1;
	
	Node getNodeA();

	Node getNodeB();

	Node getOtherNode(Node node);

	void reverse();

	int compareNodes(Edge edge);
}
