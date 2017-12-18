package nari.model.device.filter;

import nari.model.device.BsonCreator;

public interface CriteriaQuery extends SQLCreator ,BsonCreator{

	public CriteriaQuery select(Selection select);

//	public CriteriaQuery field(Field[] field);
	
	public CriteriaQuery field(Field... fields);
	
	public CriteriaQuery where(Expression exp);

	public CriteriaQuery groupBy(Field... fields);

	public CriteriaQuery orderBy(Order... order);

	public Expression toExpression();

	public Root getRoot();
}
