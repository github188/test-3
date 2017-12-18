package nari.session.access.mongo;

import java.util.Iterator;

import nari.session.access.ModelDataSource;
import nari.session.md.MappedModel;
import nari.session.md.ModelObject;
import nari.session.query.criteria.ModelQueryAttribute;

public class MongoDataSource implements ModelDataSource {

	@Override
	public void save() throws Exception {
		
	}

	@Override
	public void update() throws Exception {
		
	}

	@Override
	public void delete() throws Exception {
		
	}

	@Override
	public boolean commit() throws Exception {
		return false;
	}

	@Override
	public void rollback() throws Exception {
		
	}

	@Override
	public Iterator<ModelObject> list(MappedModel model,ModelQueryAttribute att) throws Exception {
		return null;
	}

	@Override
	public ModelObject get(MappedModel model,ModelQueryAttribute att) throws Exception {
		return null;
	}

}
