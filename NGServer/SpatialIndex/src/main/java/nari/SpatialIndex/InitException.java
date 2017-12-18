package nari.SpatialIndex;

public class InitException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -980448327796009121L;

	private String message;
	
	public InitException(String message,Throwable t) {
		super(message,t);
		this.message = message;
	}
	
	public InitException(String message) {
		super(message);
		this.message = message;
	}
	
	public InitException(Throwable t) {
		super(t);
		this.message = t.getMessage();
	}
	
	public String getMessage(){
		return message;
	}
}
