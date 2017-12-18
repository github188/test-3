package nari.graph.structure.basic;

import java.util.Iterator;
import java.util.List;

import nari.graph.structure.Graphable;
import nari.graph.structure.Graphables;

public abstract class AbstractGraphables 
	implements Graphables {
	
	protected List<Graphable> networkables;
	
	@Override
	public int size() {
		return networkables.size();
	}
	
	@Override
	public boolean isEmpty() {
		return networkables.isEmpty();
	}
	
	@Override
	public void add(Graphable component) {
		networkables.add(component);
	}

	@Override
	public void remove(Graphable component) {
		networkables.remove(component);
	}

	@Override
	public Iterator<Graphable> iterator() {
		return networkables.iterator();
	}
}
