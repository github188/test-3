package nari.parameter.BaseService.GetField;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class GetFieldRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9099346016535432854L;
	
	//设备类型
	private String[] classId = null;

	
	public String[] getClassId() {
		return classId;
	}

	public void setClassId(String[] classId) {
		this.classId = classId;
	}

	public boolean validate() {
		for(int i=0;i<classId.length;i++){
			if(StringUtils.isEmpty(classId[i])){
				return true;
			}
		}
		if(classId == null || classId.length == 0){
			return true;
		}
		return false;
	}
}
