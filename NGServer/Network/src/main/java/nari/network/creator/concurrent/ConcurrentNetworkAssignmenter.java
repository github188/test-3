package nari.network.creator.concurrent;

import java.util.Deque;
import java.util.LinkedList;

import nari.network.creator.NetworkAssignmenter;

public class ConcurrentNetworkAssignmenter
	extends NetworkAssignmenter {
	
	private Deque<TopoTask> queue = new LinkedList<TopoTask>();
	private double maxSize = 0;
	
	@Override
	public boolean isEmpty() {
		boolean empty = false;
		synchronized(queue) {
			empty = queue.isEmpty();
		}
		return empty;
	}

	@Override
	public TopoTask pop() {
		TopoTask task = null;
		synchronized(queue) {
			task = queue.poll();
		}
		return task;
	}

	@Override
	public void push(TopoTask task) {
		synchronized(queue) {
			queue.push(task); 
		}
	}

	@Override
	public String progress() {

		int progress = 0;
		if (maxSize > 0) {
			double curSize = 0;
			synchronized(queue) {
				curSize = maxSize - queue.size();
			}
			progress = (int) ((curSize / maxSize) * 20);
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
