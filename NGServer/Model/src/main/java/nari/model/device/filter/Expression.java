package nari.model.device.filter;

import org.bson.BsonDocument;

import nari.model.device.expression.Operator;

public interface Expression {

	public static final Expression NONE = new Expression(){

		@Override
		public String toResult() {
			return null;
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
			return null;
		}
		
	};
	
	public String toResult();
	
	public BsonDocument toBson();
	
	public Field getKey();
	
	public Object getValue();
	
	public Operator getOperator();
}
