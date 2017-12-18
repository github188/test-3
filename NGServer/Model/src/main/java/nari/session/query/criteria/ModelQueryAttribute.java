package nari.session.query.criteria;

public class ModelQueryAttribute {

	private Criteria crite;
	
	private String[] projections;
	
	private Operater countOp;
	
	private Group groupFilter;
	
	public ModelQueryAttribute() {
		// TODO Auto-generated constructor stub
	}

	public Criteria getCrite() {
		return crite;
	}

	public void setCrite(Criteria crite) {
		this.crite = crite;
	}

	public String[] getProjections() {
		return projections;
	}

	public void setProjections(String[] projections) {
		this.projections = projections;
	}

	public Operater getCountOp() {
		return countOp;
	}

	public void setCountOp(Operater countOp) {
		this.countOp = countOp;
	}

	public Group getGroupFilter() {
		return groupFilter;
	}

	public void setGroupFilter(Group groupFilter) {
		this.groupFilter = groupFilter;
	}
	
}
