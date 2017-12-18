package nari.graph.traverse.single;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphPath;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.basic.AbstractGraphIterator;
import nari.graph.traverse.simple.SimpleGraphPath;

/**
 * 单源广度优先遍历
 * 广度优先遍历内部维护多条轨迹
 * 
 * @author birderyu
 *
 */
public class BreadthFirstIterator 
	extends AbstractGraphIterator {
	
	/**
	 * 存储轨迹
	 * <NetworkableID, 轨迹>
	 */
	private Map<Integer, GraphPath> paths;
	
	/**
	 * 存储轨迹的长度
	 * <NetworkableID, 轨迹长度>
	 */
	private Map<Integer, Integer> pathLengths;
	
	public BreadthFirstIterator() {
		super();
		paths = new HashMap<Integer, GraphPath>();
		pathLengths = new HashMap<Integer, Integer>();
	}
	
	@Override
	public void initialize() {
		nextSet.clear();
		nextSet.addLast(getSource());
		paths.clear();
		pathLengths.clear();
	}
	
	@Override
	public Graphable next(GraphTracker tracker) {
		while (!nextSet.isEmpty()) {
			// 从邻接集合中找到一个未访问的图元
			Graphable next = nextSet.pollFirst();
			if (!tracker.isVisited(next)) {
				return next;
			}
		}
		return null;
	}
	
	@Override
	public void process(GraphTracker tracker, Graphable current, Iterator<Graphable> nexts) {
		while (nexts.hasNext()) {
			Graphable component = nexts.next();
			if (tracker.isVisited(component)) {
				continue;
			}
			nextSet.addLast(component);
		}
	}
	
	@Override
	public void addToPath(Graphable component) {
		
		if (null != getPath(component)) {
			return;
		}
		
		// 找到与之相邻的最短轨迹
		Iterator<Graphable> iter = component.getRelated();
		List<GraphPath> relatedPath = new ArrayList<GraphPath>();
		List<Integer> relatedPathLength = new ArrayList<Integer>();
		List<Integer> relatedWeight = new ArrayList<Integer>();
		
		int mixPathLength = -1;
		if (null != iter) {
			while (iter.hasNext()) {
				Graphable related = iter.next();
				GraphPath path = getPath(related);
				if (null == path) {
					continue;
				}
				int pathLength = getLength(related);
				
				if (-1 == mixPathLength) {
					mixPathLength = pathLength;
				} else {
					mixPathLength = pathLength < mixPathLength ? pathLength : mixPathLength;
				}
				
				relatedPath.add(path);
				relatedPathLength.add(pathLength);
				relatedWeight.add(related.getWeight());
			}
		}
		
		if (-1 == mixPathLength) {
			paths.put(component.getId(), new SimpleGraphPath(component));
			pathLengths.put(component.getId(), 0);
			return;
		}
		
		// 从最短的轨迹中选出一条最优轨迹
		GraphPath bestPath = null;
		int minWeight = -1;
		for (int i = 0; i < relatedPath.size(); i++) {
			if (mixPathLength == relatedPathLength.get(i)) {
				if (minWeight == -1) {
					minWeight = relatedWeight.get(i);
					bestPath = relatedPath.get(i);
				} else {
					if (minWeight > relatedWeight.get(i)) {
						minWeight = relatedWeight.get(i);
						bestPath = relatedPath.get(i);
					}
				}
			}
		}
		
		GraphPath newPath = new SimpleGraphPath(bestPath);
		newPath.push(component);
		int newPathLength = mixPathLength + component.getWeight();
		paths.put(component.getId(), newPath);
		pathLengths.put(component.getId(), newPathLength);
	}
	
	@Override
	public Graphable getLast(Graphable component) 
			throws IllegalArgumentException {
		GraphPath path = getPath(component);
		return path.top2();
	}
	
	@Override
	public GraphPath getPath(Graphable component) 
			throws IllegalArgumentException {
		if (null == component) {
			throw new IllegalArgumentException("component should not be null.");
		}
		return paths.get(component.getId());
	}
	
	private int getLength(Graphable component)
			throws IllegalArgumentException {
		if (null == component) {
			throw new IllegalArgumentException("component should not be null.");
		}
		return pathLengths.get(component.getId());
		
	}
}
