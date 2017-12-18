package nari.model.device.expression;

import java.util.regex.Pattern;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

import org.bson.BsonDocument;
import org.bson.BsonRegularExpression;

public class EWExpression implements Expression {

	private Field field = Field.NONE;
	
	private Object value = null;
	
	private boolean isExpression = false;
	
	private Expression exp = Expression.NONE;
	
	public EWExpression(Field field, Object value) {
		this.field = field;
		this.value = value;
	}
	
	public EWExpression(Field field, Expression exp) {
		this.field = field;
		this.exp = exp;
		isExpression = true;
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
			buf.append(field.getFieldName()).append(" like '%' || ").append(field.convert(value));
		}else {
			buf.append(field.getFieldName()).append(" like ").append("(").append(exp.toResult()).append(" || '%')");
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
		return Operator.ENDWITH;
	}

	@Override
	public BsonDocument toBson() {
		BsonDocument e = new BsonDocument();
		Pattern pattern = Pattern.compile("^.*" + field.convert(value) + ".*$", Pattern.CASE_INSENSITIVE);
		
		if(!isExpression){
			e.put(field.getFieldName().toUpperCase(), new BsonRegularExpression(pattern.toString()));
		}else {
			e.put(field.getFieldName().toUpperCase(), exp.toBson());
		}
		
		return e;
	}
}
