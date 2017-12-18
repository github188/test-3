package nari.model.device.filter;

import nari.model.device.expression.SQLExpression;

import org.bson.BsonDocument;

public class DefaultDBCriteriaQuery implements CriteriaQuery {

	private Selection selection = Selection.NONE;
	
	private Expression where = Expression.NONE;
	
	private Field[] group = null;
	
	private Order[] orders = null;
	
	private Field[] fields = null;
	
	public DefaultDBCriteriaQuery(){
		
	}
	
	@Override
	public CriteriaQuery select(Selection selection) {
		this.selection = selection;
		return this;
	}

	@Override
	public CriteriaQuery field(Field... fields) {
		this.fields = fields;
		return this;
	}
	
	@Override
	public CriteriaQuery where(Expression exp) {
		this.where = exp;
		return this;
	}

	@Override
	public CriteriaQuery groupBy(Field... fields) {
		this.group = fields;
		return this;
	}

	@Override
	public CriteriaQuery orderBy(Order... order) {
		this.orders = order;
		return this;
	}

	@Override
	public Expression toExpression() {
		return new SQLExpression();
	}

	@Override
	public Root getRoot() {
		return DefaultRoot.getRoot();
	}

	@Override
	public String createSQL() {
		StringBuffer buf = new StringBuffer();
		if(group==null || group.length==0){
			if(fields==null || fields.length==0){
				buf.append("select * from ").append(selection.getSelection());
			}else{
				buf.append("select ");
				int i=0;
				for(Field f:fields){
					buf.append(f.getFieldName());
					if(i<fields.length-1){
						buf.append(",");
						i++;
					}
				}
				buf.append(" from ").append(selection.getSelection());
			}
		}else{
			buf.append("select ");
			int i=0;
			for(Field f:group){
				buf.append(f.getFieldName());
				if(i<group.length-1){
					buf.append(",");
					i++;
				}
			}
			buf.append(" from ").append(selection.getSelection());
		}
		
		if(where!=Expression.NONE){
			buf.append(" where ");
			buf.append(where.toResult());
		}
		
		if(group!=null && group.length>0){
			buf.append(" group by ");
			int i=0;
			for(Field f:group){
				buf.append(f.getFieldName());
				if(i<group.length-1){
					buf.append(",");
					i++;
				}
			}
		}
		if(orders!=null && orders.length>0){
			buf.append(" order by ");
			int i=0;
			for(Order o:orders){
				buf.append(o.getOrderField().getFieldName()).append(" ").append(o.getOrder());
				if(i<orders.length-1){
					buf.append(",");
					i++;
				}
			}
		}
		
		return buf.toString();
	}

	@Override
	public BsonDocument createBson() {
//		[{$group:{_id:"$name",count:{#sum:1},total:{$sun:"$num"}}]
		return where.toBson();
	}

}
