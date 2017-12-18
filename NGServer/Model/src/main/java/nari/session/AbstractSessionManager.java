package nari.session;

import nari.session.access.DataSourceAccess;
import nari.session.md.ModelSession;

public abstract class AbstractSessionManager implements SessionManager {

	@Override
	public Session openSession() throws Exception {
		return openSession(null);
	}

	@Override
	public Session openSession(String session) throws Exception {
		DataSourceAccess ds = selectDataSource(session);
		if(ds==null){
			return null;
		}
		return new ModelSession(ds);
	}

	@Override
	public String[] sessions() throws Exception {
		DataSourceAccess[] dss = selectAllDataSource();
		
		String[] sessions = new String[dss.length];
		int i=0;
		for(DataSourceAccess ds:dss){
			sessions[i] = ds.getDataSourceName();
		}
		return sessions;
	}

	@Override
	public boolean closeSession(Session session) throws Exception {
		if(session!=null){
			session.close();
		}
		return true;
	}

	protected abstract DataSourceAccess selectDataSource(String session) throws Exception;
	
	protected abstract DataSourceAccess[] selectAllDataSource() throws Exception;
}
