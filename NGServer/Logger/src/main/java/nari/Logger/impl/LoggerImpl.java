package nari.Logger.impl;

import nari.Logger.LogWriter;
import nari.Logger.Logger;

public class LoggerImpl implements Logger {

	private String loggerName;
	
	private final LoggerWriterQueue logQueue;
	
	private org.apache.log4j.Logger log = null;
	
	public LoggerImpl(String loggerName,LoggerWriterQueue logQueue) {
		this.loggerName = loggerName;
		this.logQueue = logQueue;
		log = org.apache.log4j.Logger.getLogger(loggerName);
	}
	
	public void debug(Object message) {
		log.debug(message);
	}

	public void error(Object message) {
		log.error(message);
	}

	public void info(Object message) {
		log.info(message);
	}
	
	public void info(Object message,boolean async) {
		if(async){
			logQueue.info(this,message);
		}
	}

	public void info(LogWriter writer) {
		writer.write();
	}

	public void info(LogWriter writer, boolean async) {
		if(async){
			logQueue.info(this, writer);
		}
	}
	
	public void warn(Object message) {
		log.warn(message);
	}

	@Override
	public String getLoggerName() {
		return loggerName;
	}

	@Override
	public void debug(String message, Object... params) {
		String str = String.format(message, params);
		debug(str);
	}

	@Override
	public void error(String message, Object... params) {
		String str = String.format(message, params);
		error(str);
	}

	@Override
	public void info(String message, Object... params) {
		String str = String.format(message, params);
		info(str);
	}

	@Override
	public void warn(String message, Object... params) {
		String str = String.format(message, params);
		warn(str);
	}
}