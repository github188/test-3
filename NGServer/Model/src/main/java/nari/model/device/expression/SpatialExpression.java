package nari.model.device.expression;

import org.bson.BsonDocument;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;
import nari.Geometry.Polygon;

public class SpatialExpression implements Expression {

	private Field field;
	
	private Polygon poly;
	
	public SpatialExpression(Field field,Polygon poly){
		this.field = field;
		this.poly = poly;
	}
	
	@Override
	public String toResult() {
		return (String)field.convert(poly);
	}

	@Override
	public Field getKey() {
		return field;
	}

	@Override
	public Object getValue() {
		return poly;
	}

	@Override
	public Operator getOperator() {
		return Operator.SPATIAL;
	}

	@Override
	public BsonDocument toBson() {
		return null;
	}

}
