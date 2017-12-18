package nari.parameter.bean;

import java.io.Serializable;

public class PsrTypeInfoList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -498559686675111334L;
	
	/**
	 * 图类型ID
	 */
	private int mapId;
	
	/**
	 * 设备类型信息
	 */
	private PsrTypeInfo[] psrTypeInfos;

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public PsrTypeInfo[] getPsrTypeInfos() {
		return psrTypeInfos;
	}

	public void setPsrTypeInfos(PsrTypeInfo[] psrTypeInfos) {
		this.psrTypeInfos = psrTypeInfos;
	}
	
}
