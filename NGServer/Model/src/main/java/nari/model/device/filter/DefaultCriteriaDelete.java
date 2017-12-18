package nari.model.device.filter;

import nari.model.device.expression.SQLExpression;

public class DefaultCriteriaDelete implements CriteriaDelete,SQLCreator {

	private Selection selection = null;
	
	private Expression where = Expression.NONE;
	
	@Override
	public CriteriaDelete delete(Selection select) {
		this.selection = select;
		return this;
	}

	@Override
	public CriteriaDelete where(Expression exp) {
		where = exp==null?Expression.NONE:exp;
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
		buf.append("delete from ").append(selection.getSelection());
		
		if(where!=Expression.NONE){
			buf.append(" where ");
			buf.append(where.toResult());
		}
		return buf.toString();
	}

}
