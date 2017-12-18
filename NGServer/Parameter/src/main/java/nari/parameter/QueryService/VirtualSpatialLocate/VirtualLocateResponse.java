package nari.parameter.QueryService.VirtualSpatialLocate;

import java.io.Serializable;

import nari.parameter.bean.LocatePair;
import nari.parameter.code.ReturnCode;

public class VirtualLocateResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6367206457383890114L;

	/**
	 * 设备类型
	 */
	private String psrType = "";
	
	/**
	 * 设备 的定位信息
	 */
	private LocatePair[] location = null;

	private ReturnCode code = null;
	
	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public LocatePair[] getLocation() {
		return location;
	}

	public void setLocation(LocatePair[] location) {
		this.location = location;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
}
