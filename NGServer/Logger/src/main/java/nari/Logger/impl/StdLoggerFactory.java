package nari.Logger.impl;

import nari.Logger.Logger;
import nari.Logger.LoggerFactory;

public class StdLoggerFactory implements LoggerFactory {

	private final LoggerWriterQueue logQueue = new LoggerWriterQueue();
	
	public Logger createLogger(String loggerName) {
		return new LoggerImpl(loggerName,logQueue);
	}

}
