package nari.model.device.expression;

import org.bson.BsonDocument;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

public class NEQExpression implements Expression {

	private Field field = null;
	
	private Expression exp = null;
	
	private Object value = null;
	
	public NEQExpression(Field field, Expression exp) {
		this.field = field;
		this.exp = exp;
	}
	
	public NEQExpression(Field field, Object value) {
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
			buf.append(field.getFieldName()).append("<>").append(field.convert(value));
		}else {
			buf.append(field.getFieldName()).append("<>").append(exp.toResult());
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
		return Operator.EQ;
	}

	@Override
	public BsonDocument toBson() {
		// TODO Auto-generated method stub
		return null;
	}
}
