package nari.parameter.bean;

public class GroupTableCondition {

	private String groupNum = "";
	
	private TableCondition[] tableConditions = null;
	
	private String eachGroupCount = "";

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}


	public TableCondition[] getTableConditions() {
		return tableConditions;
	}

	public void setTableConditions(TableCondition[] tableConditions) {
		this.tableConditions = tableConditions;
	}

	public String getRecordsCount() {
		return eachGroupCount;
	}

	public void setRecordsCount(String eachGroupCount) {
		this.eachGroupCount = eachGroupCount;
	}
	
	
}
