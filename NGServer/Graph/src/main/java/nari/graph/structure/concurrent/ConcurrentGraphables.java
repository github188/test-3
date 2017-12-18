package nari.graph.structure.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.graph.structure.Graphable;
import nari.graph.structure.basic.AbstractGraphables;

public class ConcurrentGraphables 
	extends AbstractGraphables {
	
	public ConcurrentGraphables() {
		networkables = new ArrayList<Graphable>();
	}

	@Override
	public void add(Graphable component) {
		synchronized(networkables) {
			networkables.add(component);
		}
	}

	@Override
	public void remove(Graphable component) {
		synchronized(networkables) {
			networkables.remove(component);
		}
	}

	@Override
	public Iterator<Graphable> iterator() {
		List<Graphable> _networkables = new ArrayList<Graphable>();
		synchronized(networkables) {
			for (Graphable networkable : networkables) {
				_networkables.add(networkable);
			}
		}
		return _networkables.iterator();
	}
}
