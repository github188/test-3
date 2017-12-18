package nari.parameter.BaseService.GroupTable;

import java.io.Serializable;

import nari.parameter.bean.GroupTableCondition;
import nari.parameter.code.ReturnCode;

public class GroupTableResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3920619251922588331L;
	
	//分组条件
	private GroupTableCondition[] groupTableConditions = null;
	
	private ReturnCode code = null;
	

	public GroupTableCondition[] getGroupTableConditions() {
		return groupTableConditions;
	}

	public void setGroupTableConditions(GroupTableCondition[] groupTableConditions) {
		this.groupTableConditions = groupTableConditions;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	} 
	
}
