package nari.Logger;

public interface Logger {
	
	public void debug(Object message);
	
	public void debug(String message,Object... params);
	
	public void error(Object message);
	
	public void error(String message,Object... params);
	
	public void info(Object message);
	
	public void info(String message,Object... params);
	
	public void info(Object message,boolean async);
	
	public void info(LogWriter writer);
	
	public void info(LogWriter writer,boolean async);
	
	public void warn(Object message);
	
	public void warn(String message,Object... params);
	
	public String getLoggerName();
}
