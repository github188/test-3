package nari.parameter.BaseService.GroupTable;

import java.io.Serializable;

public class GroupTableRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3752140544752804069L;

	private String groupCount = "";

	public String getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(String groupCount) {
		this.groupCount = groupCount;
	}
	
	public boolean validate(){
		return false;
	}
}
