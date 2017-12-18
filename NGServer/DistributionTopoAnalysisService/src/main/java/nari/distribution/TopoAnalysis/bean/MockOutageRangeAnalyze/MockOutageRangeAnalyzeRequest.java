package nari.distribution.TopoAnalysis.bean.MockOutageRangeAnalyze;

import java.io.Serializable;

import nari.distribution.TopoAnalysis.bean.DeviceInfo;
import nari.parameter.code.PsrTypeSystem;
import nari.parameter.convert.AbstractRequest;

public class MockOutageRangeAnalyzeRequest 
	extends AbstractRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4500672965520184739L;
	
	/**
	 * 出线开关的设备类型
	 */
	private String sourceType = null;
	
	/**
	 * 出线开关的设备ID
	 */
	private String sourceId = null;
	
	/**
	 * 拉开开关的列表
	 */
	private DeviceInfo[] openSwitch = null;
	
	/**
	 * 设备类型体系
	 */
	private String psrTypeSys = PsrTypeSystem.EQUIPMENT_ID;

	@Override
	public boolean validate() {
		return null != sourceType && null != sourceId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public DeviceInfo[] getOpenSwitch() {
		return openSwitch;
	}

	public void setOpenSwitch(DeviceInfo[] openSwitch) {
		this.openSwitch = openSwitch;
	}

	public String getPsrTypeSys() {
		return psrTypeSys;
	}

	public void setPsrTypeSys(String psrTypeSys) {
		this.psrTypeSys = psrTypeSys;
	}
}
