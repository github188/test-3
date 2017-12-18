package nari.graph.traverse;

import java.util.List;

import nari.graph.structure.Graphable;

/**
 * 网络步行者
 * 网络步行者的作用是在一条网络追踪的轨迹中行走，它包含一个walk方法，其作用为
 * 根据当前轨迹中的上一个单元和当前单元，判断接下来行走的方向。
 * 相当于一个回调函数
 * 
 * @author birderyu
 *
 */
public interface GraphWalker {
	
	/**
	 * 不收集轨迹
	 */
	int DO_NOT_COLLECT_TRACE = 0;
	
	/**
	 * 收集轨迹
	 */
	int COLLECT_TRACE = 1;
	
	/**
	 * 继续向下追踪，并追踪全部的路径
	 */
	int PROCESS_EACH = 2;

	/**
	 * 继续向下追踪，但需要按照方向
	 */
	int PROCESS_DIRECTED = 4;

	/**
	 * 杀掉当前分支
	 */
	int KILL_BRANCH = 8;

	/**
	 * 立刻停止追踪
	 */
	int STOP = 16;
	
	/**
	 * 继续向下追踪，并追踪全部的路径
	 * 并收集当前单元的轨迹
	 */
	int PROCESS_EACH_AND_COLLECT_TRACE = PROCESS_EACH | COLLECT_TRACE;
	
	/**
	 * 继续向下追踪，但需要按照方向
	 * 并收集当前单元的轨迹
	 */
	int PROCESS_DIRECTED_AND_COLLECT_TRACE = PROCESS_DIRECTED | COLLECT_TRACE;
	
	/**
	 * 杀掉当前分支
	 * 并收集当前单元的轨迹
	 */
	int KILL_BRANCH_AND_COLLECT_TRACE = KILL_BRANCH | COLLECT_TRACE;
	
	/**
	 * 立刻停止追踪
	 * 并收集当前单元的轨迹
	 */
	int STOP_AND_COLLECT_TRACE = STOP | COLLECT_TRACE;
	
	/**
	 * 在当前的行走轨迹中，根据访问当前单元，并根据上一个单元与当前单元，判断下一步将要访问的单元
	 * 
	 * @param last [in] 入参，当前单元在当前轨迹中的上一个单元
	 * @param current [in] 入参，当前单元
	 * @param next [out] 出参，下一步将要访问的单元
	 * @return 判断是否需要继续行走
	 */
	int walk(Graphable last, Graphable current, List<Graphable> nexts);
}
