package nari.parameter.MainGridService.ConditionLocatQuery;

import java.io.Serializable;

import nari.parameter.bean.TypeCondition;

public class ConditionLocatQueryRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2714306384769618440L;

	/**
	 * 每种类型设备查询的条件
	 */
	private TypeCondition[] condition = null;
	
	

	public TypeCondition[] getCondition() {
		return condition;
	}

	public void setCondition(TypeCondition[] condition) {
		this.condition = condition;
	}



	//classId，OId不能为空
	
}
