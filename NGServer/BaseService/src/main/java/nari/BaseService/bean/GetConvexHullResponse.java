package nari.BaseService.bean;

import nari.parameter.code.ReturnCode;

public class GetConvexHullResponse {
	
	private ReturnCode code = null;
	
	private PsrPointCoordinate[] polygon = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public PsrPointCoordinate[] getPolygon() {
		return polygon;
	}

	public void setPolygon(PsrPointCoordinate[] polygon) {
		this.polygon = polygon;
	}

}
