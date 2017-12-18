package nari.session.access.db;

import java.util.Iterator;

import nari.session.access.ModelDataSource;
import nari.session.md.MappedModel;
import nari.session.md.ModelObject;
import nari.session.query.criteria.ModelQueryAttribute;

public class DbDataSource implements ModelDataSource {

	@Override
	public void save() throws Exception {
		
	}

	@Override
	public void update() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean commit() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rollback() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<ModelObject> list(MappedModel model,ModelQueryAttribute att) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelObject get(MappedModel model,ModelQueryAttribute att) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
