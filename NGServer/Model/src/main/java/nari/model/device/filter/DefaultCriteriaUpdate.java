package nari.model.device.filter;

import nari.model.device.expression.SQLExpression;

public class DefaultCriteriaUpdate implements CriteriaUpdate ,SQLCreator{

	private Selection selection = null;
	
	private Field[] field = null;
	
	private Object[] values = null;

	private Expression where = Expression.NONE;
	
	public DefaultCriteriaUpdate(){
		
	}
	
	@Override
	public CriteriaUpdate update(Selection select) {
		this.selection = select;
		return this;
	}

	@Override
	public CriteriaUpdate field(Field[] field) {
		this.field = field;
		return this;
	}

	@Override
	public CriteriaUpdate value(Object[] values) {
		this.values = values;
		return this;
	}

	@Override
	public CriteriaUpdate where(Expression exp) {
		this.where = exp;
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
		if(field==null || field.length==0){
			return "";
		}
		
		if(values==null || values.length==0){
			return "";
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("update ").append(selection.getSelection()).append(" set ");
		
		int i=0;
		for(Field f:field){
			buf.append(f.getFieldName()).append(" = ").append(f.convert(values[i]));
			if(i<field.length-1){
				buf.append(",");
				i++;
			}
		}
		
		if(where!=null && where!=Expression.NONE){
			buf.append(" where ").append(where.toResult());
		}
		
		return buf.toString();
	}

}
