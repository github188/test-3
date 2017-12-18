package nari.graph.structure.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nari.graph.structure.Edge;
import nari.graph.structure.Graph;
import nari.graph.structure.GraphVisitor;
import nari.graph.structure.Graphable;
import nari.graph.structure.Node;

public abstract class AbstractGraph 
	implements Graph, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4814181181180041146L;

	/**
	 * 节点的集合
	 */
	private Set<Node> nodes;
	
	/**
	 * 边的集合
	 */
	private Set<Edge> edges;

	/**
	 * 获取节点的集合
	 * @return
	 */
	@Override
	public Set<Edge> getEdges() {
		return this.edges;
	}
	
	@Override
	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public Set<Node> getNodes() {
		return this.nodes;
	}
	
	@Override
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public List<Node> getNodesOfDegree(int n) {
		final int degree = n;
		return queryNodes(new GraphVisitor() {

			@Override
			public int visit(Graphable component) {
				if (degree == ((Node)component).getDegree()) {
					return GraphVisitor.PASS_AND_CONTINUE;
				}
				return GraphVisitor.FAIL_QUERY;
			}
			
		});
	}
	
	@Override
	public List<Graphable> query(GraphVisitor visitor) {
		List<Graphable> result = new ArrayList<Graphable>();
		for (Iterator<Edge> iter = edges.iterator(); iter.hasNext(); ) {
			Edge edge = iter.next();
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
	public List<Edge> queryEdges(GraphVisitor visitor) {
		List<Edge> result = new ArrayList<Edge>();
		for (Iterator<Edge> iter = edges.iterator(); iter.hasNext(); ) {
			Edge edge = iter.next();
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
	public List<Node> queryNodes(GraphVisitor visitor) {
		List<Node> result = new ArrayList<Node>();
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
		visitEdges(visitor);
		visitNodes(visitor);
	}

	@Override
	public void visitEdges(GraphVisitor visitor) {
		for (Iterator<Edge> iter = edges.iterator(); iter.hasNext(); ) {
			visitor.visit((Graphable)iter.next());
		}
	}

	@Override
	public void visitNodes(GraphVisitor visitor) {
		for (Iterator<Node> iter = nodes.iterator(); iter.hasNext(); ) {
			visitor.visit((Graphable)iter.next());
		}
	}
	
	@Override
	public int size() {
		return nodes.size() + edges.size();
	}
}
