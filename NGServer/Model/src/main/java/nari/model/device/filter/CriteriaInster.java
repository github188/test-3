package nari.model.device.filter;

public interface CriteriaInster {

	public CriteriaInster insert(Selection select);

	public CriteriaInster field(Field[] field);
	
	public CriteriaInster value(Object[] values);
	
	public Expression toExpression();

	public Root getRoot();
}
