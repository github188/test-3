package nari.model.device.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonDocument;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

public class ORExpression implements Expression {

	private List<Expression> expList = new ArrayList<Expression>();

	public ORExpression(Expression exp1, Expression exp2) {
		expList.add(exp1);
		expList.add(exp2);
	}
	
	public ORExpression(Expression[] exps) {
		Collections.addAll(expList, exps);
	}

	@Override
	public String toResult() {
		StringBuffer buf = new StringBuffer();
		int i=0;
		buf.append("(");
		for(Expression exp:expList){
			buf.append(exp.toResult());
			if(i<expList.size()-1){
				buf.append(" or ");
				i++;
			}
		}
		buf.append(")");
		return buf.toString();
	}

	@Override
	public Field getKey() {
		return null;
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public Operator getOperator() {
		return Operator.OR;
	}

	@Override
	public BsonDocument toBson() {
		BsonDocument doc = new BsonDocument();
		BsonArray tmp = new BsonArray();
		for(Expression exp:expList){
			tmp.add(exp.toBson());
		}
		
		doc.append("$or", tmp);
		return doc;
	}
	
}
