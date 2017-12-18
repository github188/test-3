package nari.parameter.bean;

import java.io.Serializable;

public class PSRDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8145871538952231116L;

	/**
	 * 设备类型种类
	 */
	private String psrType = "";
	
	/**
	 * 设备类型别名
	 */
	private String psrName = "";
	
	/**
	 * 设备类型id
	 */
	private String name = ""; 
	
	/**
	 * 设备子类型信息
	 */
	private SubPSRDef[] subPSRDef = null;

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getPsrName() {
		return psrName;
	}

	public void setPsrName(String psrName) {
		this.psrName = psrName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubPSRDef[] getSubPSRDef() {
		return subPSRDef;
	}

	public void setSubPSRDef(SubPSRDef[] subPSRDef) {
		this.subPSRDef = subPSRDef;
	}
	
}
