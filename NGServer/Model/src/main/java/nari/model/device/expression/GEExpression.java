package nari.model.device.expression;

import org.bson.BsonDocument;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

public class GEExpression implements Expression {

	private Field field = null;
	
	private Expression exp = null;
	
	private Object value = null;
	
	public GEExpression(Field field, Expression exp) {
		this.field = field;
		this.exp = exp;
	}
	
	public GEExpression(Field field, Object value) {
		this.field = field;
		this.value = value;
	}

	@Override
	public String toResult() {
		if(field==Field.NONE){
			return "";
		}
		if(exp==Expression.NONE && value==null){
			return "";
		}
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		if(value!=null){
			buf.append(field.getFieldName()).append(">=").append(field.convert(value));
		}else {
			buf.append(field.getFieldName()).append(">=").append(exp.toResult());
		}
		buf.append(")");
		return buf.toString();
	}

	@Override
	public Field getKey() {
		return field;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Operator getOperator() {
		return Operator.GRATEROREQUAL;
	}

	@Override
	public BsonDocument toBson() {
		
		
		
		
		
		
		
		return null;
	}
}
