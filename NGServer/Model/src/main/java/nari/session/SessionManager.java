package nari.session;

public interface SessionManager {

	public Session openSession() throws Exception;
	
	public Session openSession(String session) throws Exception;
	
	public String[] sessions() throws Exception;
	
	public boolean closeSession(Session session) throws Exception;
}
