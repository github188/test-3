package nari.parameter.MapService.GetMapConfig;

import java.io.Serializable;

import nari.parameter.bean.RasterMap;
import nari.parameter.code.ReturnCode;

public class MapConfigResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7899798256343596253L;

	/**
	 * 地图信息
	 */
	private RasterMap map = null;
	
	private ReturnCode code = null;

	public RasterMap getMap() {
		return map;
	}

	public void setMap(RasterMap map) {
		this.map = map;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
}
