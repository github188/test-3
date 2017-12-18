package nari.model.device.filter;

public interface CriteriaUpdate {

	public CriteriaUpdate update(Selection select);

	public CriteriaUpdate field(Field[] field);
	
	public CriteriaUpdate value(Object[] values);
	
	public CriteriaUpdate where(Expression exp);

	public Expression toExpression();

	public Root getRoot();
	
}
