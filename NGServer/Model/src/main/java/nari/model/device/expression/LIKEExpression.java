package nari.model.device.expression;

import org.bson.BsonDocument;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

public class LIKEExpression implements Expression {

	private Field field = Field.NONE;
	
	private Object value = null;
	
	private Expression exp = Expression.NONE;
	
	public LIKEExpression(Field field, Object value) {
		this.field = field;
		this.value = value;
	}
	
	public LIKEExpression(Field field, Expression exp) {
		this.field = field;
		this.exp = exp;
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
			buf.append(field.getFieldName()).append(" like '%' || ").append(field.convert(value)).append(" || '%'");
		}else {
			buf.append(field.getFieldName()).append(" like ").append("('%' || ").append(exp.toResult()).append(" || '%')");
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
		return Operator.LIKE;
	}

	@Override
	public BsonDocument toBson() {
		// TODO Auto-generated method stub
		return null;
	}
}
