package nari.network.structure;

import java.util.List;

import nari.graph.traverse.GraphWalker;
import nari.network.device.TopoDevice;

/**
 * 拓扑网络追踪者
 * 
 * @author birderyu
 *
 */
public interface NetworkWalker {
	
	/**
	 * 继续向下追踪，并追踪全部的路径
	 */
	int PROCESS_EACH = GraphWalker.PROCESS_EACH;

	/**
	 * 继续向下追踪，但需要按照方向
	 */
	int PROCESS_DIRECTED = GraphWalker.PROCESS_DIRECTED;

	/**
	 * 杀掉当前分支
	 */
	int KILL_BRANCH = GraphWalker.KILL_BRANCH;

	/**
	 * 立刻停止追踪
	 */
	int STOP = GraphWalker.STOP;
	
	/**
	 * 继续向下追踪，并追踪全部的路径
	 * 并收集当前单元的轨迹
	 */
	int PROCESS_EACH_AND_COLLECT_TRACE = GraphWalker.PROCESS_EACH_AND_COLLECT_TRACE;
	
	/**
	 * 继续向下追踪，但需要按照方向
	 * 并收集当前单元的轨迹
	 */
	int PROCESS_DIRECTED_AND_COLLECT_TRACE = GraphWalker.PROCESS_DIRECTED_AND_COLLECT_TRACE;
	
	/**
	 * 杀掉当前分支
	 * 并收集当前单元的轨迹
	 */
	int KILL_BRANCH_AND_COLLECT_TRACE = GraphWalker.KILL_BRANCH_AND_COLLECT_TRACE;
	
	/**
	 * 立刻停止追踪
	 * 并收集当前单元的轨迹
	 */
	int STOP_AND_COLLECT_TRACE = GraphWalker.STOP_AND_COLLECT_TRACE;
	
	/**
	 * 追踪回调方法：访问当前设备，并根据当前设备，以及位于当前轨迹中的上一个设备，判断下一步将要访问的设备
	 * 
	 * <p>这个方法由拓扑分析算法的提供者重写
	 * 
	 * @param last 
	 * 		[in] 入参，当前设备在当前轨迹中的上一个设备
	 * @param current 
	 * 		[in] 入参，当前设备
	 * @param nexts 
	 * 		[out] 出参，下一步将要访问的设备，<b>仅当返回值为{@code PROCESS_DIRECTION}或{@code PROCESS_DIRECTION_AND_COLLECT_TRACE}，
	 * 		表示需要限定方向继续进行追踪时，才需要维护这个参数。</b>
	 * 
	 * @return 返回值应为下面八个值的其中之一：<br>
	 * * {@code PROCESS_EACH} 无方向地前进，即继续追踪，并且追踪所有的方向；<br>
	 * * {@code PROCESS_DIRECTED} 限定方向地前进，即继续追踪，但只追踪位于{@code nexts}中的设备；<br>
	 * * {@code KILL_BRANCH} 杀掉当前分支，比如遇到开关断开时，需要放回该值，表示该方向不能继续追踪下去；<br>
	 * * {@code STOP} 停止追踪，比如遇到停止条件时，需要返回该值，表示立刻停止追踪；<br>
	 * * {@code PROCESS_EACH_AND_COLLECT_TRACE} 同 {@code PROCESS_EACH}，同时需要将当前设备的轨迹收集上来；<br>
	 * * {@code PROCESS_DIRECTED_AND_COLLECT_TRACE} 同 {@code PROCESS_DIRECTED}，同时需要将当前设备的轨迹收集上来；<br>
	 * * {@code KILL_BRANCH_AND_COLLECT_TRACE} 同 {@code KILL_BRANCH}，同时需要将当前设备的轨迹收集上来；<br>
	 * * {@code STOP_AND_COLLECT_TRACE} 同 {@code STOP}，同时需要将当前设备的轨迹收集上来。<br>
	 * 
	 * <p>绝大多数情况下的追踪，都是<b>无方向地追踪</b>，此时不需要维护{@code nexts}参数，因为可以通过获取与{@code current}
	 * 相邻接的所有设备来获取继续追踪的设备。仅当少数情况下，才会需要<b>限定方向地前进</b>，如追踪遇到变压器的情况下。
	 * <b>杀掉当前分支</b>和<b>停止追踪</b>这两个状态，往往可以控制追踪的流程，达到按需追踪的目的。需要注意的是，<b>杀掉
	 * 当前分支</b>并非意味着被杀掉的分支就一定无法被追踪到，有可能别的分支依然能够追踪到。
	 * 
	 */
	int walk(TopoDevice last, TopoDevice current, List<TopoDevice> nexts);
}
