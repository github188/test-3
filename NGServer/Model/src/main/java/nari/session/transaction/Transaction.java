package nari.session.transaction;

public interface Transaction {

	public void commit() throws Exception;
	
	public void rollback() throws Exception;
	
	public void begin() throws Exception;
}
