package nari.network.creator;

import nari.network.creator.concurrent.ConcurrentNetworkAssignmenter;
import nari.network.creator.simple.SimpleNetworkAssignmenter;

/**
 * 任务分配
 * @author birderyu
 *
 */
public abstract class NetworkAssignmenter {

	/**
	 * 一个拓扑任务
	 * @author birderyu
	 *
	 */
	public static class TopoTask {
		
		private int modelId;
		private String tableName;
		
		public TopoTask(int modelId, String tableName) {
			super();
			this.modelId = modelId;
			this.tableName = tableName;
		}
		
		public int getModelId() {
			return modelId;
		}
		public void setModelId(int modelId) {
			this.modelId = modelId;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
	}
	
	public static NetworkAssignmenter createAssignmenter(int threadNum) {
		if (threadNum <= 1) {
			return new SimpleNetworkAssignmenter();
		}
		return new ConcurrentNetworkAssignmenter();
	}
	
	public abstract boolean isEmpty();
	
	/**
	 * 取出一个任务
	 * @return 若任务已经结束，则返回null
	 */
	public abstract TopoTask pop();
	
	/**
	 * 将一个任务加入列表
	 * @param task
	 */
	public abstract void push(TopoTask task);
	
	/**
	 * 获取当前进度条，直接打印即可
	 * 
	 * @return
	 */
	public abstract String progress();
	
	/**
	 * 
	 * @param size
	 */
	public abstract void setMaxSize(int size);
}
