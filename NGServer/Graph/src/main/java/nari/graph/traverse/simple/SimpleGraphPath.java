package nari.graph.traverse.simple;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphPath;

public class SimpleGraphPath 
	implements GraphPath {
	
	private Deque<Graphable> components;
	
	public SimpleGraphPath() {
		components = new LinkedList<Graphable>();
	}
	
	public SimpleGraphPath(Graphable component) {
		components = new LinkedList<Graphable>();
		push(component);
	}
	
	public SimpleGraphPath(GraphPath path) {
		components = new LinkedList<Graphable>();
		append(path);
	}
	
	@Override
	public boolean isEmpty() {
		return components.isEmpty();
	}
	
	@Override
	public int size() {
		return components.size();
	}

	@Override
	public void push(Graphable component) {
		components.addLast(component);
	}

	@Override
	public Graphable pop() {
		return components.pollLast();
	}
	
	@Override
	public void append(GraphPath path) {
		Iterator<Graphable> iter = path.iterator();
		while (iter.hasNext()) {
			push(iter.next());
		}
	}
	
	@Override
	public Graphable top() {
		return components.peekLast();
	}
	
	@Override
	public Graphable top2() {
		Graphable top = pop();
		Graphable top2 = top();
		if (null != top) {
			push(top);
		}
		return top2;
	}

	@Override
	public void clear() {
		components.clear();
	}

	@Override
	public Iterator<Graphable> iterator() {
		return components.iterator();
	}
	
}
