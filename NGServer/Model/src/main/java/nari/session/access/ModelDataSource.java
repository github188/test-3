package nari.session.access;

import java.util.Iterator;

import nari.session.md.MappedModel;
import nari.session.md.ModelObject;
import nari.session.query.criteria.ModelQueryAttribute;

public interface ModelDataSource {

	public Iterator<ModelObject> list(MappedModel model,ModelQueryAttribute att) throws Exception;
	
	public ModelObject get(MappedModel model,ModelQueryAttribute att) throws Exception;
	
	public void save() throws Exception;
	
	public void update() throws Exception;
	
	public void delete() throws Exception;
	
	public boolean commit() throws Exception;
	
	public void rollback() throws Exception;
}
