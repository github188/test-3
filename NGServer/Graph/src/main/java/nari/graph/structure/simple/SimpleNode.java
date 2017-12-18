package nari.graph.structure.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.graph.structure.Edge;
import nari.graph.structure.Graphable;
import nari.graph.structure.Node;

public class SimpleNode 
	extends SimpleGraphable implements Node {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 780977327638575659L;
	
	/**
	 * 与该节点相关联的边
	 */
	private transient List<Edge> edges;
	
	/**
	 * 创建一个网络节点
	 */
	public SimpleNode(int id) {
		super(id);
		edges = new ArrayList<Edge>();
	}

	@Override
	public void add(Edge e) {
		edges.add(e);
	}

	@Override
	public int getDegree() {
		int degree = 0;
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge e = iter.next();
			if (e.getNodeA().equals(this)) {
				degree++;
			}
			if (e.getNodeB().equals(this)) {
				degree++;
			}
		}
		return degree;
	}

	@Override
	public Edge getEdge(Node n) {
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge e = iter.next();
			if ((e.getNodeA().equals(this) && e.getNodeB().equals(n)) ||
					(e.getNodeA().equals(n) && e.getNodeB().equals(this))) {
				return e;
			}
		}
		return null;
	}

	@Override
	public List<Edge> getEdges() {
		return this.edges;
	}

	@Override
	public List<Edge> getEdges(Node node) {
		List<Edge> edges = new ArrayList<Edge>();
		Iterator<Edge> iter = this.edges.iterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();
			if ((edge.getNodeA().equals(this) && edge.getNodeB().equals(node)) ||
					(edge.getNodeA().equals(node) && edge.getNodeB().equals(this))) {
				edges.add(edge);
			}
		}
		return edges;
	}

	@Override
	public void remove(Edge e) {
		this.edges.remove(e);
	}
	
	@Override
	public Iterator<Graphable> getRelated() {
		
		// 为了能查找到edge，令node的临界元件为edge
		List<Graphable> related = new ArrayList<Graphable>(edges.size());
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();
			if (null != edge) {
				related.add(edge);
			}
		}
		return related.iterator();
	}

	@Override
	public boolean relateTo(Graphable component) {
		// node只与edge相邻接
		if (component instanceof Edge) {
			Edge edge = (Edge) component;
			for (Edge _edge : edges) {
				if (edge.equals(_edge)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getWeight() {
		return 0;
	}

}
