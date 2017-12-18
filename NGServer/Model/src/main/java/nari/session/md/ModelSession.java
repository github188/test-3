package nari.session.md;

import nari.session.AbstractSession;
import nari.session.access.DataSourceAccess;
import nari.session.access.ModelDataSource;

public class ModelSession extends AbstractSession {

	private DataSourceAccess ds = null;
	
	public ModelSession(DataSourceAccess ds) {
		this.ds = ds;
	}

	@Override
	protected ModelDataSource getDataSource() throws Exception {
		return ds==null?null:ds.getDataSource();
	}

	@Override
	protected MappedModel findModel(String name,boolean create) throws Exception {
		
		return null;
	}

	@Override
	protected boolean validModelName(String name) throws Exception {
		return false;
	}
}
