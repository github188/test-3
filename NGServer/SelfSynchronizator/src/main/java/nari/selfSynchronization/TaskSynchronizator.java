package nari.selfSynchronization;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.selfSynchronization.bean.TaskQueue;
import nari.selfSynchronization.taskConsumer.TaskExecutor;
import nari.selfSynchronization.taskProducer.TaskHunter;

/**
 * 任务同步类
 * @author xxxxcl
 *
 */
public class TaskSynchronizator {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	//生产和消费线程数
	private int producerThreadNum = 10;
	private int consumerThreadNum = 10;
	
	public  void Synchronize(){
		ExecutorService executor = Executors.newFixedThreadPool(producerThreadNum + consumerThreadNum);
		CountDownLatch producerThreadsSignal = new CountDownLatch(producerThreadNum);
		CountDownLatch consumerThreadsSignal = new CountDownLatch(consumerThreadNum);
		logger.info("开启更新查询线程数量：" + producerThreadNum);
		logger.info("开启同步更新线程数量：" + consumerThreadNum);
		
		TaskQueue taskQueue = new TaskQueue();
		
		//分线程创建生产者搜寻任务
		for(int i=0;i<producerThreadNum;i++){
			TaskHunter taskHunter = new TaskHunter(i,taskQueue,producerThreadsSignal);
			executor.execute(taskHunter);
		}
		
		//分线程创建消费者消费任务
		for(int i=0;i<consumerThreadNum;i++){
			TaskExecutor taskExecutor = new TaskExecutor(i,taskQueue,consumerThreadsSignal);
			executor.execute(taskExecutor);
		}
	}
}
