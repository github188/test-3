package nari.parameter.MainGridService.FuzzyQuery;

import java.io.Serializable;

public class FuzzyQueryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5476770616782392586L;

	String fuzzyString = "";
	
	//所属网省条件
	String[] provinceCondition = null;

	public String getFuzzyString() {
		return fuzzyString;
	}

	public void setFuzzyString(String fuzzyString) {
		this.fuzzyString = fuzzyString;
	}

	public String[] getProvinceCondition() {
		return provinceCondition;
	}

	public void setProvinceCondition(String[] provinceCondition) {
		this.provinceCondition = provinceCondition;
	}


}
