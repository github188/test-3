package nari.QueryService.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class GetAllSubSetRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4945736897179876785L;

	private String SBID = "";
	
	
	private String pclassId = "";
	
	//显示子设备类型
	private String[] disPlayRModelIds = null;
	
	//是否拓扑查询
	private String isTopu = "";
		
	//关系种类()
//	private RelationType relationType = RelationType.CONTAIN;
	private RelationType relationType = null;
	
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

//	public String getSubRelateField() {
//		return subRelateField;
//	}
//
//	public void setSubRelateField(String subRelateField) {
//		this.subRelateField = subRelateField;
//	}

	public boolean validate(){
		if(StringUtils.isEmpty(SBID)){
			return false;
		}
		if(relationType == null){
			return false;
		}
		return true;
	}

	

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	public String getIsTopu() {
		return isTopu;
	}

	public void setIsTopu(String isTopu) {
		this.isTopu = isTopu;
	}

}
