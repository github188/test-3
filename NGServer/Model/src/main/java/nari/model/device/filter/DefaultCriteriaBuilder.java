package nari.model.device.filter;

import nari.model.device.expression.ANDExpression;
import nari.model.device.expression.AscOrder;
import nari.model.device.expression.DescOrder;
import nari.model.device.expression.EQExpression;
import nari.model.device.expression.EWExpression;
import nari.model.device.expression.GEExpression;
import nari.model.device.expression.GTExpression;
import nari.model.device.expression.INExpression;
import nari.model.device.expression.LEExpression;
import nari.model.device.expression.LIKEExpression;
import nari.model.device.expression.LOWERExpression;
import nari.model.device.expression.LTExpression;
import nari.model.device.expression.NEQExpression;
import nari.model.device.expression.NLIKEExpression;
import nari.model.device.expression.NNULLExpression;
import nari.model.device.expression.NULLExpression;
import nari.model.device.expression.ORExpression;
import nari.model.device.expression.SWExpression;
import nari.model.device.expression.SpatialExpression;
import nari.model.device.expression.UPPERExpression;
import nari.Geometry.Polygon;

public class DefaultCriteriaBuilder implements CriteriaBuilder {

	public DefaultCriteriaBuilder(){
		
	}
	
	@Override
	public CriteriaQuery createQuery() {
		return new DefaultDBCriteriaQuery();
	}

	@Override
	public Order asc(Field field) {
		return new AscOrder(field);
	}

	@Override
	public Order desc(Field field) {
		return new DescOrder(field);
	}

	@Override
	public Expression count() {
		return null;
	}

	@Override
	public Expression distinct(Field field) {
		return null;
	}

//	@Override
//	public Expression in(Field field, Expression exp) {
//		return new INExpression(field,exp);
//	}

	@Override
	public Expression in(Field field, Object[] values) {
		return new INExpression(field,values);
	}

	@Override
	public Expression and(Expression exp1, Expression exp2) {
		return new ANDExpression(exp1,exp2);
	}

	@Override
	public Expression and(Expression[] exps) {
		return new ANDExpression(exps);
	}

	@Override
	public Expression or(Expression exp1, Expression exp2) {
		return new ORExpression(exp1,exp2);
	}

	@Override
	public Expression or(Expression[] exps) {
		return new ORExpression(exps);
	}

	@Override
	public Expression isNull(Field field) {
		return new NULLExpression(field);
	}

	@Override
	public Expression isNotNull(Field field) {
		return new NNULLExpression(field);
	}

//	@Override
//	public Expression equal(Field field, Expression exp) {
//		return new EQExpression(field,exp);
//	}

	@Override
	public Expression equal(Field field, Object value) {
		return new EQExpression(field,value);
	}

//	@Override
//	public Expression notEqual(Field field, Expression exp) {
//		return new NEQExpression(field,exp);
//	}

	@Override
	public Expression notEqual(Field field, Object value) {
		return new NEQExpression(field,value);
	}

	@Override
	public Expression greaterThan(Field field, Expression exp) {
		return new GTExpression(field,exp);
	}

	@Override
	public Expression greaterThan(Field field, Object value) {
		return new GTExpression(field,value);
	}

//	@Override
//	public Expression greaterOrEqual(Field field, Expression exp) {
//		return new GEExpression(field,exp);
//	}

	@Override
	public Expression greaterOrEqual(Field field, Object value) {
		return new GEExpression(field,value);
	}

//	@Override
//	public Expression lessThan(Field field, Expression exp) {
//		return new LTExpression(field,exp);
//	}

	@Override
	public Expression lessThan(Field field, Object value) {
		return new LTExpression(field,value);
	}

//	@Override
//	public Expression lessOrEqual(Field field, Expression exp) {
//		return new LEExpression(field,exp);
//	}

	@Override
	public Expression lessOrEqual(Field field, Object value) {
		return new LEExpression(field,value);
	}

//	@Override
//	public Expression between(Field field, Object value1, Object value2) {
//		return null;
//	}

//	@Override
//	public Expression like(Field field, Expression exp) {
//		return new LIKEExpression(field, exp);
//	}

//	@Override
//	public Expression notLike(Field field, Expression exp) {
//		return new NLIKEExpression(field, exp);
//	}

//	@Override
//	public Expression startWith(Field field, Expression exp) {
//		return new SWExpression(field,exp);
//	}

//	@Override
//	public Expression endWidth(Field field, Expression exp) {
//		return new EWExpression(field, exp);
//	}

	@Override
	public Expression like(Field field, Object value) {
		return new LIKEExpression(field,value);
	}

	@Override
	public Expression notLike(Field field, Object value) {
		return new NLIKEExpression(field,value);
	}

	@Override
	public Expression startWith(Field field, Object value) {
		return new SWExpression(field,value);
	}

	@Override
	public Expression endWidth(Field field, Object value) {
		return new EWExpression(field,value);
	}

	@Override
	public Expression lower(Object value) {
		return new LOWERExpression(value);
	}

	@Override
	public Expression upper(Object value) {
		return new UPPERExpression(value);
	}

	@Override
	public Expression spatial(Field field, Polygon poly) {
		return new SpatialExpression(field,poly);
	}

	@Override
	public CriteriaInster createInsert() {
		return new DefaultCriteriaInster();
	}

	@Override
	public CriteriaUpdate createUpdate() {
		return new DefaultCriteriaUpdate();
	}

	@Override
	public CriteriaDelete createDelete() {
		return new DefaultCriteriaDelete();
	}

	@Override
	public Root getRoot() {
		return DefaultRoot.getRoot();
	}
	
}
