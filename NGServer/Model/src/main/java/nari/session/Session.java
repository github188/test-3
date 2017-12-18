package nari.session;

import nari.session.md.MappedModel;
import nari.session.md.ModelObject;
import nari.session.md.ModelObjectArray;
import nari.session.query.Query;
import nari.session.query.criteria.Criteria;
import nari.session.transaction.Transaction;

public interface Session {

	public void beginTransaction() throws Exception;
	
	public void commit() throws Exception;
	
	public void rollback() throws Exception;
	
	public Query createQuery(MappedModel model) throws Exception;
	
	public Query createQuery(String modelName) throws Exception;
	
	public Transaction getTransaction() throws Exception;
	
	public void save(ModelObject obj) throws Exception;
	
	public void save(ModelObjectArray objArr) throws Exception;
	
	public void update(ModelObject obj) throws Exception;
	
	public void update(ModelObject obj,Criteria c) throws Exception;
	
	public void delete(ModelObject obj) throws Exception;
	
	public void delete(ModelObject obj,Criteria c) throws Exception;
	
	public void close() throws Exception;
	
	public boolean isClose() throws Exception;
	
	public MappedModel getModel(String name) throws Exception;
	
	public void removeModel(MappedModel model) throws Exception;
	
	public void removeModel(String name) throws Exception;
}
