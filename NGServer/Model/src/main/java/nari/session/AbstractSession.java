package nari.session;

import nari.session.access.ModelDataSource;
import nari.session.md.MappedModel;
import nari.session.md.ModelObject;
import nari.session.md.ModelObjectArray;
import nari.session.query.CriteriaQuery;
import nari.session.query.Query;
import nari.session.query.criteria.Criteria;
import nari.session.transaction.Transaction;

public abstract class AbstractSession implements Session {

	public AbstractSession() {
		
	}

	@Override
	public void beginTransaction() throws Exception {
		
	}

	@Override
	public void commit() throws Exception {
		
	}

	@Override
	public void rollback() throws Exception {
		
	}

	@Override
	public Query createQuery(MappedModel model) throws Exception {
		if(model==null){
			return null;
		}
		return new CriteriaQuery(model,getDataSource());
	}

	@Override
	public Query createQuery(String modelName) throws Exception {
		MappedModel model = getModel(modelName);
		if(model==null){
			return null;
		}
		return createQuery(model);
	}
	
	@Override
	public Transaction getTransaction() throws Exception {
		return null;
	}

	@Override
	public void save(ModelObject obj) throws Exception {
		
	}

	@Override
	public void save(ModelObjectArray objArr) throws Exception {
		
	}

	@Override
	public void update(ModelObject obj) throws Exception {
		
	}

	@Override
	public void update(ModelObject obj, Criteria c) throws Exception {
		
	}
	
	@Override
	public void delete(ModelObject obj) throws Exception {
		
	}

	@Override
	public void delete(ModelObject obj, Criteria c) throws Exception {
		
	}
	
	@Override
	public void close() throws Exception {
		
	}

	@Override
	public boolean isClose() throws Exception {
		return false;
	}

	@Override
	public MappedModel getModel(String name) throws Exception {
		if(!validModelName(name)){
			return null;
		}
		return findModel(name,true);
	}
	
	@Override
	public void removeModel(MappedModel model) throws Exception {
		
	}
	
	@Override
	public void removeModel(String name) throws Exception {
		
	}
	
	protected abstract boolean validModelName(String name) throws Exception;
	
	protected abstract MappedModel findModel(String name,boolean create) throws Exception;
	
	protected abstract ModelDataSource getDataSource() throws Exception;
}
