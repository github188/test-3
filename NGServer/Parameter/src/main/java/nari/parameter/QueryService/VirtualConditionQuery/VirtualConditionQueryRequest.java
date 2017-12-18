package nari.parameter.QueryService.VirtualConditionQuery;

import java.io.Serializable;

import nari.parameter.bean.SubPSRDef;

/**
 * 查询虚拟设备包含的设备
 * @author zwl
 *
 */
public class VirtualConditionQueryRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1446484733185236975L;

	/**
	 * 线路、电缆、间隔、导线等的设备类型 
	 */
	private String virtualType = "";
	
	/**
	 * 需要查询的设备子类型
	 */
	private SubPSRDef[] subTypes = null;

	public String getVirtualType() {
		return virtualType;
	}

	public void setVirtualType(String virtualType) {
		this.virtualType = virtualType;
	}

	public SubPSRDef[] getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(SubPSRDef[] subTypes) {
		this.subTypes = subTypes;
	}
	
}
