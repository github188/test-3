package nari.selfSynchronization.taskProducer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.selfSynchronization.SelfSynchronizationServiceActivator;
import nari.selfSynchronization.bean.Task;
import nari.selfSynchronization.bean.TaskQueue;
import nari.selfSynchronization.bean.TaskRequest;

/**
 * 任务搜寻线程
 * @author xxxxcl
 *
 */
public class TaskHunter implements Runnable {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor dbAdaptor = SelfSynchronizationServiceActivator.dbAdaptor;

	private int threadId;
	private TaskQueue taskQueue;	//同步更新任务队列
	private CountDownLatch threadsSignal;	//同步更新线程信号
	boolean SynchronizeFlag = true;	//是否需要开启oracle,Mongo同步更新功能

	public TaskHunter(int threadId, TaskQueue taskQueue,
			CountDownLatch threadsSignal) {
		super();
		this.threadId = threadId;
		this.taskQueue = taskQueue;
		this.threadsSignal = threadsSignal;
	}

	private void hunt() {
		// serchDB（每个线程搜寻一条记录）
		Integer TaskOID = hasTask(threadId);
		if (TaskOID != null) { // 如果有DefineTask
			// 得到任务
			Task[] tasks = defTask(TaskOID);
			// 将Task放入taskQueue
			for (int i = 0; i < tasks.length; i++) {
				taskQueue.push(tasks[i]);
			}
		}
	}

	@Override
	public void run() {

		while (SynchronizeFlag) {
			// 每10s查询一次是否需要更新（oracle的物化日志）
			hunt();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		threadsSignal.countDown();
		logger.info(" 线程" + String.valueOf(threadId + 1) + "运行结束。");
	}

	/**
	 * 搜索数据库看书否需要有更新任务（根据任务表）
	 * 
	 * @return
	 */
	private Integer hasTask(int rownum) {
//		if(rownum == 1){
//			System.out.println("a");
//		}
		// 根据T_DW_CIMSVG_TASK任务信息表判断是有任务
		String hasTaskSql = "select OID,STATE from DWZY.T_DW_CIMSVG_TASK where STATE = 20 and rownum = ?";
		Map<String, Object> hasTaskMap = new HashMap<String, Object>();
		try {
			Object[] params = new Object[] { rownum };
			hasTaskMap = dbAdaptor.findMap(hasTaskSql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if (hasTaskMap != null) {
			int state = Integer.valueOf(String.valueOf(hasTaskMap.get("state")));
			if (state == 20) {
				Integer OID = Integer.valueOf(String.valueOf(hasTaskMap.get("oid")));
				return OID;
			}
		}
		return null;
	}

	/**
	 * 判断需要更新什么样的任务（根据任务详情表）
	 * 
	 * @param taskReq
	 * @return
	 */
	public Task[] defTask(int taskOID) {
		// 根据T_DW_CIMSVG_TASK任务信息表判断是有任务
		String hasTaskSql = "select psruri,datatype,dtype from DWZY.T_DW_CIMSVG_TASK_DETAIL where TASK_ID = ?";
		List<Map<String, Object>> taskMessageList = new ArrayList<Map<String, Object>>();
		try {
			Object[] params = new Object[] { taskOID };
			taskMessageList = dbAdaptor.findAllMap(hasTaskSql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		List<Task> taskList = new ArrayList<Task>();
		if (taskMessageList != null && taskMessageList.size() != 0) {
			
			for (int i = 0, count = taskMessageList.size(); i < count; i++) {	//根据记录装载Task
				TaskRequest req = new TaskRequest();
				String id = String.valueOf(taskMessageList.get(i)
						.get("psruri"));
				int dataType = Integer.valueOf(String.valueOf(taskMessageList.get(i).get("datatype")));
				int taskType = 0;
				switch (dataType) {
				case 0:
					taskType = 1;
					req.setDocumentId(id);
					break;
				case 1:
					taskType = 2;
					req.setOID(id);
					break;
				}
				String mapid = String.valueOf(taskMessageList.get(i).get("dtype"));
				req.setMapId(mapid);
				Task task = new Task(String.valueOf(taskOID), taskType, req);
				taskList.add(task);
			}
		}
		Task[] tasks = new Task[taskList.size()];
		tasks = taskList.toArray(tasks);
		return tasks;
	}
}
