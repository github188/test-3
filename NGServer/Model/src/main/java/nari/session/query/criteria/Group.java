package nari.session.query.criteria;

public interface Group {

	public void having(Operater op) throws Exception;
	
	public Operater getOperater();
	
	public String[] getGroupKey();
}
