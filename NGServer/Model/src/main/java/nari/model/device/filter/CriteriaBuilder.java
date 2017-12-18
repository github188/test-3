package nari.model.device.filter;

public interface CriteriaBuilder {

	public CriteriaQuery createQuery();

	public CriteriaInster createInsert();
	
	public CriteriaUpdate createUpdate();
	
	public CriteriaDelete createDelete();
	
	public Order asc(Field field);

	public Order desc(Field field);

	public Expression count();

	public Expression distinct(Field field);

//	public Expression in(Field field,Expression exp);
	
	public Expression in(Field field,Object[] values);

	public Expression and(Expression exp1,Expression exp2);

	public Expression and(Expression[] exps);

	public Expression or(Expression exp1,Expression exp2);

	public Expression or(Expression[] exps);

	public Expression isNull(Field field);

	public Expression isNotNull(Field field);

//	public Expression equal(Field field,Expression exp);

	public Expression equal(Field field,Object value);

//	public Expression notEqual(Field field,Expression exp);

	public Expression notEqual(Field field,Object value);

	public Expression greaterThan(Field field,Expression exp);

	public Expression greaterThan(Field field, Object value);

//	public Expression greaterOrEqual(Field field,Expression exp);

	public Expression greaterOrEqual(Field field, Object value);

//	public Expression lessThan(Field field,Expression exp);

	public Expression lessThan(Field field, Object paramY);

//	public Expression lessOrEqual(Field field,Expression exp);

	public Expression lessOrEqual(Field field, Object value);

//	public Expression between(Field field, Object value1, Object value2);
	
//	public Expression like(Field field,Expression exp);

//	public Expression notLike(Field field,Expression exp);
	
//	public Expression startWith(Field field,Expression exp);
	
//	public Expression endWidth(Field field,Expression exp);
	
	public Expression like(Field field,Object value);

	public Expression notLike(Field field,Object value);
	
	public Expression startWith(Field field,Object value);
	
	public Expression endWidth(Field field,Object value);
	
	public Expression lower(Object value);
	
	public Expression upper(Object value);

	public Expression spatial(Field field,nari.Geometry.Polygon poly);
	
	public Root getRoot();
}
