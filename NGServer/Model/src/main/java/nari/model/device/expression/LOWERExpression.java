package nari.model.device.expression;

import org.bson.BsonDocument;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

public class LOWERExpression implements Expression {

	private Object value = null;
	
	public LOWERExpression(Object value) {
		this.value = value;
	}

	@Override
	public String toResult() {
		return String.valueOf(value).toLowerCase();
	}

	@Override
	public Field getKey() {
		return null;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Operator getOperator() {
		return Operator.LOWER;
	}

	@Override
	public BsonDocument toBson() {
		// TODO Auto-generated method stub
		return null;
	}
}
