package nari.network.creator.simple;

import java.util.Deque;
import java.util.LinkedList;

import nari.network.creator.NetworkAssignmenter;

public class SimpleNetworkAssignmenter
	extends NetworkAssignmenter {
	
	private Deque<TopoTask> queue = new LinkedList<TopoTask>();
	private double maxSize = 0;
	
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public TopoTask pop() {
		return queue.poll();
	}

	@Override
	public void push(TopoTask task) {
		queue.push(task); 
	}

	@Override
	public String progress() {
		int progress = 0;
		if (maxSize > 0) {
			progress = (int) (((maxSize - queue.size()) / maxSize) * 20);
		}

		StringBuilder sProgress = new StringBuilder("[");
		for (int i = 0; i < progress; i++) {
			sProgress.append('=');
		}
		for (int i = progress; i < 20; i++) {
			sProgress.append(' ');
		}
		sProgress.append(']');
		return sProgress.toString();
	}

	@Override
	public void setMaxSize(int size) {
		maxSize = size;
	}
}
