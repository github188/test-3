package nari.model.device.expression;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

import org.bson.BsonDocument;
import org.bson.BsonNull;

public class NULLExpression implements Expression {

	private Field field = Field.NONE;
	
	public NULLExpression(Field field) {
		this.field = field;
	}

	@Override
	public String toResult() {
		return "("+field.getFieldName()+" is null)";
	}

	@Override
	public Field getKey() {
		return field;
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public Operator getOperator() {
		return Operator.NULL;
	}

	@Override
	public BsonDocument toBson() {
		return new BsonDocument().append(field.getFieldName().toUpperCase(), new BsonNull());
	}
}
