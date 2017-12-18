package nari.parameter.bean;

public class FilterGroup {

	private FilterExp[] exps;
	
	public FilterGroup(FilterExp...exps){
		this.exps = exps;
	}
	
	public FilterExp[] getFilters(){
		return exps;
	}
}

