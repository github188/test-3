package nari.graph.structure.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.graph.structure.Edge;
import nari.graph.structure.Graphable;
import nari.graph.structure.Node;

public class ConcurrentEdge 
	extends ConcurrentGraphable implements Edge {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5752699986802235463L;
	
	private Node nodeA;
	private Node nodeB;
	
	public ConcurrentEdge(int id, Node nodeA, Node nodeB) {
		super(id);
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}

	@Override
	public Iterator<Graphable> getRelated() {
		
		// 为了能查找到node，令edge的临界元件为node
		List<Graphable> related = new ArrayList<Graphable>(2);
		if (nodeA != null) related.add(nodeA);
		if (nodeB != null) related.add(nodeB);
		return related.iterator();
	}

	@Override
	public int compareNodes(Edge e) {
		if (this.nodeA.equals(e.getNodeA()) && this.nodeB.equals(e.getNodeB())) {
			// 当前边与e相同，且方向相同（nodeA、nodeB）
			return EQUAL_NODE_ORIENTATION;
		} else if (this.nodeA.equals(e.getNodeB()) && this.nodeB.equals(e.getNodeA())) {
			// 当前边与e相同，且方向相反（nodeA、nodeB）
			return OPPOSITE_NODE_ORIENTATION;
		}
		// 当前边与e相同
		return UNEQUAL_NODE_ORIENTATION;
	}

	@Override
	public Node getNodeA() {
		return this.nodeA;
	}

	@Override
	public Node getNodeB() {
		return this.nodeB;
	}

	@Override
	public Node getOtherNode(Node node) {
		if (node.equals(this.nodeA)) {
			return this.nodeB;
		} else if (node.equals(this.nodeB)) {
			return this.nodeA;
		}
		return null;
	}

	@Override
	public void reverse() {
		Node n = this.nodeA;
		this.nodeA = this.nodeB;
		this.nodeB = n;
	}

	@Override
	public boolean relateTo(Graphable component) {
		// edge只与node相邻接
		if (component instanceof Node) {
			Node node = (Node) component;
			if (node.equals(nodeA) || node.equals(nodeB)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getWeight() {
		return 1;
	}
}
