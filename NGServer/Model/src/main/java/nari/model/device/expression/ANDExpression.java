package nari.model.device.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;

import org.bson.BsonArray;
import org.bson.BsonDocument;

public class ANDExpression implements Expression {

	private List<Expression> expList = new ArrayList<Expression>();

	public ANDExpression(Expression exp1, Expression exp2) {
		expList.add(exp1);
		expList.add(exp2);
	}
	
	public ANDExpression(Expression[] exps) {
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
				buf.append(" and ");
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
		return null;
	}

	@Override
	public BsonDocument toBson() {
		BsonDocument doc = new BsonDocument();
		BsonArray tmp = new BsonArray();
		for(Expression exp:expList){
			tmp.add(exp.toBson());
		}
		
		doc.append("$and", tmp);
		
		return doc;
	}
}
