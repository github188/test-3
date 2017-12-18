package nari.model.device.filter;

import nari.model.device.expression.SQLExpression;

public class DefaultCriteriaInster implements CriteriaInster ,SQLCreator{

	private Selection selection = null;
	
	private Field[] field = null;
	
	private Object[] values = null;
	
	@Override
	public CriteriaInster insert(Selection select) {
		this.selection = select;
		return this;
	}

	@Override
	public CriteriaInster field(Field[] field) {
		this.field = field;
		return this;
	}

	@Override
	public CriteriaInster value(Object[] values) {
		this.values = values;
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
		buf.append("inster into ").append(selection.getSelection());
		
		String s = "";
		int i=0;
		for(Field f:field){
			s = s+f.getFieldName();
			if(i<field.length-1){
				s=s+",";
			}
		}
		buf.append("(").append(s).append(")");
		String v = "";
		int j = 0;
		for(Object val:values){
			v = v + field[j].convert(val);
		}
		buf.append("values(").append(v).append(")");
		
		return buf.toString();
	}

}
