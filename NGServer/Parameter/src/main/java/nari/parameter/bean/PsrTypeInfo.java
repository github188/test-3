package nari.parameter.bean;

import java.io.Serializable;

public class PsrTypeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4329177595769734258L;
	
	/**
	 * 设备类型ID
	 */
	private String psrTypeId;
	
	/**
	 * 设备类型名称
	 */
	private String psrTypeName;
	
	/**
	 * 设备类型别名
	 */
	private String psrTypeAlias;

	public String getPsrTypeId() {
		return psrTypeId;
	}

	public void setPsrTypeId(String psrTypeId) {
		this.psrTypeId = psrTypeId;
	}

	public String getPsrTypeName() {
		return psrTypeName;
	}

	public void setPsrTypeName(String psrTypeName) {
		this.psrTypeName = psrTypeName;
	}
	
	public String getPsrTypeAlias() {
		return psrTypeAlias;
	}

	public void setPsrTypeAlias(String psrTypeAlias) {
		this.psrTypeAlias = psrTypeAlias;
	}

}
