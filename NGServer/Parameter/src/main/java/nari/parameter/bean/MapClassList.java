package nari.parameter.bean;

import java.io.Serializable;

public class MapClassList implements Serializable {

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
	private ClassInfo[] classInfos;

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public ClassInfo[] getClassInfos() {
		return classInfos;
	}

	public void setClassInfos(ClassInfo[] classInfos) {
		this.classInfos = classInfos;
	}
	
}
