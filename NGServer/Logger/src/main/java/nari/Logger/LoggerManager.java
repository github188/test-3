package nari.Logger;

import nari.Logger.impl.StdLoggerFactory;

public class LoggerManager {

	private static LoggerHolder loggerHolder = new LoggerHolder(new StdLoggerFactory());
	
	public static Logger getLogger(Class<?> klass){
		return loggerHolder.get(klass.getName());
	}
	
	public static Logger getLogger(String loggerName){
		return loggerHolder.get(loggerName);
	}
	
	public static void main(String[] args) {
//		long s = System.currentTimeMillis();
//		for(int i=0;i<100000;i++){
//			org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(i+"");
//			log.info(LoggerManager.class.getName());
//		}
//		
//		long e = System.currentTimeMillis();
//		
//		System.out.println("log4j:"+(e-s)+"ms");
		
		
//		long s = System.currentTimeMillis();
//		for(int i=0;i<100000;i++){
//			Logger logger = LoggerManager.getLogger(i+"");
//			logger.info(LoggerManager.class.getName());
//		}
//		long e = System.currentTimeMillis();
//		
//		System.out.println("logger:"+(e-s)+"ms");
	}
}
