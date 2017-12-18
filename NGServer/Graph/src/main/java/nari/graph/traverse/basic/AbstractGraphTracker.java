package nari.graph.traverse.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.graph.structure.Graph;
import nari.graph.structure.Graphable;
import nari.graph.traverse.GraphIterator;
import nari.graph.traverse.GraphPath;
import nari.graph.traverse.GraphTracker;
import nari.graph.traverse.GraphWalker;
import nari.graph.traverse.simple.SimpleGraphPath;

public abstract class AbstractGraphTracker 
	implements GraphTracker {
	
	private Graph graph;
	private GraphWalker walker;
	private GraphIterator iterator;
	
	public AbstractGraphTracker(Graph graph, GraphWalker walker, GraphIterator iterator) {
		this.graph = graph;
		this.walker = walker;
		this.iterator = iterator;
	}

	@Override
	public Graph getGraph() {
		return graph;
	}
	
	@Override
	public GraphWalker getWalker() {
		return walker;
	}
	
	@Override
	public GraphIterator getIterator() {
		return iterator;
	}
	
	@Override
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	@Override
	public void setWalker(GraphWalker walker) {
		this.walker = walker;
	}

	@Override
	public void setIterator(GraphIterator iterator) {
		this.iterator = iterator;
	}
	
	@Override
	public boolean track(Graphable source) {
		return track(source, null);
	}
	
	@Override
	public boolean track(Graphable source, Map<Graphable, GraphPath> traces) {
		
		if (null == source) {
			throw new IllegalArgumentException("sources should not be null.");
		}
		
		// 初始化迭代器
		iterator.setSource(source);
		iterator.initialize();
		
		// 初始化一些变量
		Graphable last = null; // 当前访问的单元在当前轨迹中的上一个单元
		Graphable current = null; // 当前访问的单元
		List<Graphable> nexts = new ArrayList<Graphable>(); // 当前访问的单元在当前轨迹中即将要访问到的单元
		boolean finished = false; // 是否查询成功
		
		// 开始进行追踪
		while (!finished && 
				((current = iterator.next(this)) != null)) {
			
			// 将当前单元设置为已访问过
			setVisited(current, true);
			
			// 将当前单元添加进轨迹的集合
			iterator.addToPath(current);

			// 获取当前单元在轨迹上的上一个单元
			last = iterator.getLast(current);
			
			// 访问当前单元
			int status = walker.walk(last, current, nexts);
			if (collectTrace(status, traces)) {
				collectTrace(current, traces);
			}
			
			switch (status) {
			case GraphWalker.PROCESS_EACH:
			case GraphWalker.PROCESS_EACH_AND_COLLECT_TRACE:
				iterator.process(this, current);
				break;
			case GraphWalker.PROCESS_DIRECTED:
			case GraphWalker.PROCESS_DIRECTED_AND_COLLECT_TRACE:
				iterator.process(this, current, nexts.iterator());
				nexts.clear(); // 将即将要访问到的单元清空
				break;
			case GraphWalker.KILL_BRANCH:
			case GraphWalker.KILL_BRANCH_AND_COLLECT_TRACE:
				iterator.killBranch(this, current);
				break;
			case GraphWalker.STOP:
			case GraphWalker.STOP_AND_COLLECT_TRACE:
				return true;
			default:
				throw new IllegalStateException("Unrecognized return value from GraphWalker");
			}
		}
		
		return false;
	}

	@Override
	public void track(Graphable[] sources) {
		track(sources, null);
	}
	
	@Override
	public void track(Graphable[] sources, Map<Graphable, GraphPath> traces) 
			throws IllegalArgumentException {
		
		if (null == sources) {
			throw new IllegalArgumentException("sources should not be null.");
		}
		
		for (Graphable source : sources) {
			track(source, traces);
		}
	}
	
	/**
	 * 是否需要收集轨迹
	 * 
	 * @param status
	 * @return
	 */
	private boolean collectTrace(int status, Map<Graphable, GraphPath> traces) {
		if (null == traces) {
			return false;
		}
		return (status & GraphWalker.COLLECT_TRACE) == GraphWalker.COLLECT_TRACE;
	}

	/**
	 * 收集轨迹进入轨迹的集合
	 * 
	 * @param component
	 * @param traces
	 */
	private void collectTrace(Graphable component, Map<Graphable, GraphPath> traces) {
		if (null == component || null == traces) {
			return;
		}
		traces.put(component, new SimpleGraphPath(iterator.getPath(component)));
	}
}
