package nari.Logger;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoggerHolder {

	private ConcurrentMap<String,CounterLogger> map = new ConcurrentHashMap<String,CounterLogger>();
	
	private final ExecutorService exec;
	
	private final int size = 100;
	
	private int index = 0;
	
	private LoggerFactory factory;
	
	private Lock lock = new ReentrantLock();
	
	private Condition condition = null;
	
	public LoggerHolder(LoggerFactory factory) {
		this.factory = factory;
		condition = lock.newCondition();
		exec = Executors.newSingleThreadExecutor();
		exec.submit(new LoggerCleanTask());
	}
	
	public Logger get(String loggerName){
		Logger logger = null;
		if (!map.containsKey(loggerName)){
			index++;
			Logger log = factory.createLogger(loggerName);
			CounterLogger clog = new CounterLogger(log);
			map.put(loggerName, clog);
			logger = log;
		}else{
			CounterLogger clog = map.get(loggerName);
			clog.countUp();
			logger = clog.getLog();
		}
		
		if(index==size){
			try {
				lock.lock();
				condition.signal();
			} finally {
				lock.unlock();
			}
		}
		return logger;
	}
	
	private class LoggerCleanTask implements Runnable{

		@Override
		public void run() {
			try {
				lock.lock();
				while(true){
					condition.await();
					
					SortedMap<Integer, CounterLogger> treeMap = new TreeMap<>();
					CounterLogger log = null;
					
					for(Map.Entry<String, CounterLogger> entry:map.entrySet()){
						log = entry.getValue();
						treeMap.put(log.getCount(), log);
					}
					
					int i = 0;
					
					for(Map.Entry<Integer, CounterLogger> entry:treeMap.entrySet()){
						if(i<50){
							i++;
						}else{
							break;
						}
						map.remove(entry.getValue().getLog().getLoggerName());
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
		
	}
	
	public static class CounterLogger{
		
		private Logger log;
		
		private final AtomicInteger count = new AtomicInteger(0);
		
		public CounterLogger(Logger log){
			this.log = log;
		}

		public Logger getLog() {
			return log;
		}
		
		public int getCount(){
			return count.get();
		}

		public int countUp(){
			return count.incrementAndGet();
		}
		
	}
	
}
