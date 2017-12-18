package nari.MongoQuery.QueryService.bean;

import java.io.Serializable;

import nari.parameter.code.PsrTypeSystem;

/**
 * 查询关系设备请求参数
 * @author biederyu
 *
 */
public class QueryRelationsRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7464013345381965160L;

	/**
	 * 设备类型
	 */
	private String psrType;
	
	/**
	 * 设备ID
	 */
	private String psrId;
	
	/**
	 * 关系类型（可为空）
	 * 为空则表示查询所有关系类型
	 */
	private String[] relationTypes = null;
	
	/**
	 * 关联字段（可为空）
	 * 关联字段指定所有关联的设备的字段，若不指定，则表示查询每一个字段
	 */
	private String[] relationFields = null;
	
	/**
	 * 
	 */
	private String[] displayPsrTypes = null;
	
	/**
	 * 
	 */
	private String psrTypeSys = PsrTypeSystem.EQUIPMENT_ID;

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getPsrId() {
		return psrId;
	}

	public void setPsrId(String psrId) {
		this.psrId = psrId;
	}

	public String[] getRelationTypes() {
		return relationTypes;
	}

	public void setRelationTypes(String[] relationTypes) {
		this.relationTypes = relationTypes;
	}

	public String[] getRelationFields() {
		return relationFields;
	}

	public void setRelationField(String[] relationFields) {
		this.relationFields = relationFields;
	}
	
	public String[] getDisplayPsrTypes() {
		return displayPsrTypes;
	}
	
	public void setDisplayPsrTypes(String[] displayPsrTypes) {
		this.displayPsrTypes = displayPsrTypes;
	}

	public String getPsrTypeSys() {
		return psrTypeSys;
	}

	public void setPsrTypeSys(String psrTypeSys) {
		this.psrTypeSys = psrTypeSys;
	}
}
