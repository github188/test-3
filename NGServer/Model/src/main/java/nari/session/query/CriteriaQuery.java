package nari.session.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.session.access.ModelDataSource;
import nari.session.md.MappedModel;
import nari.session.md.ModelObject;
import nari.session.query.criteria.CountOperater;
import nari.session.query.criteria.Criteria;
import nari.session.query.criteria.Group;
import nari.session.query.criteria.KeyGroup;
import nari.session.query.criteria.ModelQueryAttribute;
import nari.session.query.criteria.Operater;

public class CriteriaQuery implements Query {

	private MappedModel model = null;
	
	private ModelDataSource dataSource = null;
	
	private final ModelQueryAttribute att = new ModelQueryAttribute();
	
	public CriteriaQuery(MappedModel model,ModelDataSource dataSource) {
		this.model = model;
		this.dataSource = dataSource;
	}
	
	@Override
	public List<ModelObject> list() throws Exception {
		Iterator<ModelObject> it = dataSource.list(model,att);
		if(it==null){
			return null;
		}
		
		List<ModelObject> list = new ArrayList<ModelObject>();
		while(it.hasNext()){
			list.add(it.next());
		}
		
		return list;
	}

	@Override
	public ModelObject first() throws Exception {
		return dataSource.get(model,att);
	}

	@Override
	public Query projection(String[] names) throws Exception {
		att.setProjections(names);
		return this;
	}

	@Override
	public Group group(String... groupKey) throws Exception {
		Group group = new KeyGroup(groupKey);
		att.setGroupFilter(group);
		return group;
	}

	@Override
	public Query count(String key) throws Exception {
		Operater countOp = new CountOperater();
		att.setCountOp(countOp);
		return this;
	}

	@Override
	public Query filter(Criteria crite) throws Exception {
		att.setCrite(crite);
		return this;
	}

}
