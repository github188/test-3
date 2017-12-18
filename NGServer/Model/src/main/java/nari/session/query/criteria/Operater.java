package nari.session.query.criteria;

public interface Operater {

	public String getOperater();
	
	public String getKey();
	
	public Object getValue();
	
	public boolean isArray();
	
	public Object[] getArray();
}
