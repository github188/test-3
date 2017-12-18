package nari.model.device.expression;

import java.util.regex.Pattern;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

import org.bson.BsonDocument;
import org.bson.BsonRegularExpression;

public class NNULLExpression implements Expression {

	private Field field = Field.NONE;
	
	public NNULLExpression(Field field) {
		this.field = field;
	}

	@Override
	public String toResult() {
		return "("+field.getFieldName()+" is not null)";
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
		return Operator.NOTNULL;
	}

	@Override
	public BsonDocument toBson() {
		Pattern pattern = Pattern.compile("^.*", Pattern.CASE_INSENSITIVE);
		return new BsonDocument().append(field.getFieldName().toUpperCase(), new BsonRegularExpression(pattern.toString()));
	}
}
