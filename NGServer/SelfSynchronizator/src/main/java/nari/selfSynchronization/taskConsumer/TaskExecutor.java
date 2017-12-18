package nari.selfSynchronization.taskConsumer;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.MongoDBUpdate.bean.UdateFeederDevRequest;
import nari.MongoDBUpdate.bean.UpdateThematicRequest;
import nari.MongoDBUpdate.handler.UdateFeederDevHandler;
import nari.MongoDBUpdate.handler.UpdateThematicHandler;
import nari.selfSynchronization.SelfSynchronizationServiceActivator;
import nari.selfSynchronization.bean.Task;
import nari.selfSynchronization.bean.TaskQueue;
import nari.selfSynchronization.bean.TaskRequest;

public class TaskExecutor implements Runnable {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor dbAdaptor = SelfSynchronizationServiceActivator.dbAdaptor;

	private int threadId;
	private TaskQueue taskQueue;	//任务队列
	private CountDownLatch threadsSignal;	//线程信号
	boolean SynchronizeFlag = true;	//是否开启同步

	public TaskExecutor(int threadId, TaskQueue taskQueue,
			CountDownLatch threadsSignal) {
		super();
		this.threadId = threadId;
		this.taskQueue = taskQueue;
		this.threadsSignal = threadsSignal;
	}

	@Override
	public void run() {

		// 无限轮询更新
		while (SynchronizeFlag) {
			Task task = searchQueue();
			if (task == null) {	//若没有任务，休息50ms继续轮询
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {	//若有任务，执行任务
				if (executeTask(task)) {	//执行更新任务
					if (UpdTaskDB(task.getTaskOID())) {	//若更新任务成功，更新数据库的任务表
						logger.info("更新成功一条任务ID为" + task.getTaskOID() + "的记录");
					} else {
						logger.error("更新失败一条任务ID为" + task.getTaskOID() + "的记录");
					}
				}
			}
		}
		threadsSignal.countDown();
		logger.info(" 线程" + String.valueOf(threadId + 1) + "运行结束。");

	}

	/**
	 * 从任务队列获取任务
	 * 
	 * @return
	 */
	private Task searchQueue() {
		if (!taskQueue.isEmpety()) {
			return taskQueue.poll();
		}
		return null;
	}

	/**
	 * 执行任务
	 * 
	 * @param task
	 * @return
	 */
	private boolean executeTask(Task task) {
		boolean flag = false;
		// judge Task
		switch (judgeTask(task)) {
		case 1:
//			flag = updateThematicDev(task.getReq());
			logger.info("专题图更新完成");
			flag = true;
			break;
		case 2:
//			flag = updateFeederDev(task.getReq());
			logger.info("馈线更新完成");
			flag = true;
			break;
		}
		return flag;
	}

	/**
	 * 更新完之后更新任务表对应记录
	 * (线程安全)
	 * @return
	 */
	private synchronized boolean UpdTaskDB(String taskOID) {
		// 更新任务表
		String updTaskSql = "update DWZY.T_DW_CIMSVG_TASK set STATE = 30 where oid = ?";
		Object[] params = new Object[]{taskOID};
		try {
			if(dbAdaptor.update(updTaskSql, params)){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 判断是何种任务
	 * 
	 * @param task
	 * @return
	 */
	public int judgeTask(Task task) {
		return task.getTaskType();
	}

	/**
	 * 更新大馈线任务
	 * 
	 * @param req
	 * @return
	 */
	public boolean updateFeederDev(TaskRequest req) {
		UdateFeederDevRequest updReq = new UdateFeederDevRequest();
		String OID = req.getOID();
		if (!"".equalsIgnoreCase(OID) && OID != null) {
			updReq.setOID(OID);
		}

		String SBID = req.getSBID();
		if (!"".equalsIgnoreCase(SBID) && SBID != null) {
			updReq.setSBID(SBID);
		}

		String pmodelId = req.getPmodelId();
		if (!"".equalsIgnoreCase(pmodelId) && pmodelId != null) {
			updReq.setPmodelId(pmodelId);
		}

		UdateFeederDevHandler handler = new UdateFeederDevHandler();
		try {
			int reSultCode = handler.udateFeederDev(updReq).getCode().getCode();
			if (reSultCode == 1000) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新专题图任务
	 * 
	 * @param req
	 * @return
	 */
	public boolean updateThematicDev(TaskRequest req) {
		UpdateThematicRequest updReq = new UpdateThematicRequest();
		String mapId = req.getMapId();
		if (!"".equalsIgnoreCase(mapId) && mapId != null) {
			updReq.setMapId(mapId);
		}

		String documentId = req.getDocumentId();
		if (!"".equalsIgnoreCase(documentId) && documentId != null) {
			String[] documentIds = new String[] { documentId };
			updReq.setDocumentId(documentIds);
		}

		UpdateThematicHandler handler = new UpdateThematicHandler();
		try {
			int reSultCode = handler.updateThematic(updReq).getCode().getCode();
			if (reSultCode == 1000) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
