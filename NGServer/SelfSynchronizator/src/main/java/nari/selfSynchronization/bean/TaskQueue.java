package nari.selfSynchronization.bean;

import java.util.AbstractQueue;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 任务队列
 * @author xxxxcl
 *
 */
public class TaskQueue {
	
	private Deque<Task> queue = new LinkedList<Task>();
//	private Map<String,Integer> taskCount = new HashMap<String,Integer>();
//	private Map<String,Integer> hasUpdtaskCount = new HashMap<String,Integer>();
	
	public boolean isEmpety(){
		boolean empty = false;
		synchronized(queue) {
			empty = queue.isEmpty();
		}
		return empty;
	}
	
	public void push(Task task){
		synchronized(queue) {
			queue.push(task); 
		}
	}
	
	public Task poll(){
		Task task = null;
		synchronized(queue) {
			task = queue.pollLast();
		}
		return task;
	}
//
//	public Map<String, Integer> getTaskCount() {
//		return taskCount;
//	}
//
//	public void setTaskCount(Map<String, Integer> taskCount) {
//		this.taskCount = taskCount;
//	}
//
//	public Map<String, Integer> getHasUpdtaskCount() {
//		return hasUpdtaskCount;
//	}
//
//	public void setHasUpdtaskCount(Map<String, Integer> hasUpdtaskCount) {
//		this.hasUpdtaskCount = hasUpdtaskCount;
//	}
//	
}
