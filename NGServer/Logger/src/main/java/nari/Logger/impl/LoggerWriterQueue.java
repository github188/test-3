package nari.Logger.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.Logger.LogWriter;
import nari.Logger.Logger;

public class LoggerWriterQueue {

	private final ArrayBlockingQueue<AsyncLog> queue = new ArrayBlockingQueue<AsyncLog>(500);
	
	private final ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public LoggerWriterQueue() {
		exec.submit(new AsyncLogTask());
	}
	
	public void info(Logger log,Object message){
		try {
			queue.put(new AsyncLog(log,message));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void info(Logger log,LogWriter writer){
		try {
			queue.put(new AsyncLog(log,writer));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	class AsyncLogTask implements Runnable{

		@Override
		public void run() {
			try {
				AsyncLog alog = queue.take();
				
				Logger log = alog.getLog();
				Object msg = alog.getLogMsg();
				
				if(msg instanceof LogWriter){
					LogWriter writer = (LogWriter)msg;
					writer.write();
				}else{
					log.info(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		
	}
	
	class AsyncLog{
		
		private Logger log;
		
		private Object logMsg;

		public AsyncLog(Logger log,Object logMsg) {
			this.log = log;
			this.logMsg = logMsg;
		}
		
		public Logger getLog() {
			return log;
		}

		public void setLog(Logger log) {
			this.log = log;
		}

		public Object getLogMsg() {
			return logMsg;
		}

		public void setLogMsg(Object logMsg) {
			this.logMsg = logMsg;
		}
		
	}
}
