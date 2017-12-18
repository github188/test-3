package nari.model.device.expression;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

import org.bson.BsonDocument;
import org.bson.BsonString;

public class EQExpression implements Expression {

	private Field field = null;
	
	private Expression exp = null;
	
	private Object value = null;
	
	private boolean isExpression = false;
	
	public EQExpression(Field field, Expression exp) {
		this.field = field;
		this.exp = exp;
		isExpression = true;
	}
	
	public EQExpression(Field field, Object value) {
		this.field = field;
		this.value = value;
		isExpression = false;
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
		if(!isExpression){
			buf.append(field.getFieldName()).append("=").append(field.convert(value));
		}else {
			buf.append(field.getFieldName()).append("=").append(exp.toResult());
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
		BsonDocument e = new BsonDocument();
		
		if(field==Field.NONE){
			return null;
		}
		
		if(!isExpression){
			e.put(field.getFieldName(),new BsonString(String.valueOf(field.convert(value))));
		}else {
			e.put(field.getFieldName(),exp.toBson());
		}
		
		return e;
	}
}
