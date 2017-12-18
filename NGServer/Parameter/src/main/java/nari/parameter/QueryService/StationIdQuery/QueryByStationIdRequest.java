package nari.parameter.QueryService.StationIdQuery;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class QueryByStationIdRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7368125545105756165L;
	
	private String SBID = "";
	
	
	private String pclassId = "";
	
	//显示子设备类型
	private String[] disPlayRModelIds = null;
	
	//是否拓扑查询
	private String isTopu = "";
	
	
	public String[] getDisPlayRModelIds() {
		return disPlayRModelIds;
	}

	public void setDisPlayRModelIds(String[] disPlayRModelIds) {
		this.disPlayRModelIds = disPlayRModelIds;
	}

	public String getSBID() {
		return SBID;
	}

	public void setSBID(String sBID) {
		SBID = sBID;
	}

	public String getPclassId() {
		return pclassId;
	}

	public void setPclassId(String pclassId) {
		this.pclassId = pclassId;
	}
	
	public String getIsTopu() {
		return isTopu;
	}

	public void setIsTopu(String isTopu) {
		this.isTopu = isTopu;
	}

	public boolean validate(){
		if(StringUtils.isEmpty(SBID)){
			return true;
		}
		return false;
	}
	
}
