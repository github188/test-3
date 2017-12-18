package nari.SpatialIndex;

public class StopException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -980448327796009121L;

	private String message;
	
	public StopException(String message,Throwable t) {
		super(message,t);
		this.message = message;
	}
	
	public StopException(String message) {
		super(message);
		this.message = message;
	}
	
	public StopException(Throwable t) {
		super(t);
		this.message = t.getMessage();
	}
	
	public String getMessage(){
		return message;
	}
}
