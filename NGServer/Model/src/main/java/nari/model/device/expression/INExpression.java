package nari.model.device.expression;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

public class INExpression implements Expression {

	private Field field = null;
	
//	private Expression exp = null;
	
	private Object[] values = null;
	
//	private boolean isExpression = false;
	
//	public INExpression(Field field,Expression exp){
//		this.field = field;
//		this.exp = exp;
//		isExpression = true;
//	}
	
	public INExpression(Field field,Object[] values){
		this.field = field;
		this.values = values;
	}

	@Override
	public String toResult() {
		if(field==Field.NONE){
			return "";
		}
//		if(exp==Expression.NONE && values==null){
//			return "";
//		}
		StringBuffer buf = new StringBuffer();
		buf.append("(");
//		if(!isExpression){
			StringBuffer bf = new StringBuffer();
			int i=0;
			for(Object value:values){
				bf.append(field.convert(value));
				i++;
				if(i<values.length){
					bf.append(",");
				}
			}
			buf.append(field.getFieldName()).append(" in (").append(bf.toString()).append(")");
//		}else {
//			buf.append(field.getFieldName()).append(" in ").append(exp.toResult());
//		}
		buf.append(")");
		return buf.toString();
	}

	@Override
	public Field getKey() {
		return field;
	}

	@Override
	public Object getValue() {
		return values;
	}

	@Override
	public Operator getOperator() {
		return Operator.IN;
	}

	@Override
	public BsonDocument toBson() {
		BsonDocument e = new BsonDocument();
//		if(!isExpression){
			BsonArray arr = new BsonArray();
			for(Object value:values){
				arr.add(new BsonString(String.valueOf(field.convert(value))));
			}
			e.append(field.getFieldName().toUpperCase(), new BsonDocument().append("$in", arr));
//		}
		
		return e;
	}
	
}
