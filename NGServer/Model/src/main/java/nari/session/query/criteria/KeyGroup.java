package nari.session.query.criteria;

public class KeyGroup implements Group {

	private String[] groupKey = null;
	
	private Operater op = null;
	
	public KeyGroup(String[] groupKey) {
		this.groupKey = groupKey;
	}
	
	@Override
	public void having(Operater op) throws Exception {
		this.op = op;
	}

	@Override
	public Operater getOperater() {
		return op;
	}

	@Override
	public String[] getGroupKey() {
		return groupKey;
	}

}
