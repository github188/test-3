package nari.SpatialIndex;

public class StartException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -980448327796009121L;

	private String message;
	
	public StartException(String message,Throwable t) {
		super(message,t);
		this.message = message;
	}
	
	public StartException(String message) {
		super(message);
		this.message = message;
	}
	
	public StartException(Throwable t) {
		super(t);
		this.message = t.getMessage();
	}
	
	public String getMessage(){
		return message;
	}
}
