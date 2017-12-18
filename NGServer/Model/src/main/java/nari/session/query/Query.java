package nari.session.query;

import java.util.List;

import nari.session.md.ModelObject;
import nari.session.query.criteria.Criteria;
import nari.session.query.criteria.Group;

public interface Query {

	public List<ModelObject> list() throws Exception;
	
	public ModelObject first() throws Exception;
	
	public Query filter(Criteria crite) throws Exception;
	
	public Query projection(String[] names) throws Exception;
	
	public Group group(String...groupKey) throws Exception;
	
	public Query count(String key) throws Exception;
	
}
