package nari.QueryService.bean;

import java.io.Serializable;

import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.TypeCondition;

public class SpatialqueryVirtualRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5031992569373113548L;

	private String token = "";
	
	/**
	 * 空间对象
	 */
	private GeometryPair geom = null;
	
	/**
	 * 搜索的条件
	 */
	private TypeCondition[] conds = null;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public GeometryPair getGeom() {
		return geom;
	}

	public void setGeom(GeometryPair geom) {
		this.geom = geom;
	}

	public TypeCondition[] getConds() {
		return conds;
	}

	public void setConds(TypeCondition[] conds) {
		this.conds = conds;
	}
	
	public boolean validate(){
		if(geom == null){
			return true;
		}
		if(geom.getCoords() == null || geom.getCoords().length == 0 || geom.getGeometryType() == null){
			return true;
		}
		for(int i=0;i<conds.length;i++){
			if(conds == null || conds.length == 0){
				return true;
			}
			if(conds[i].getPsrType().equals("") || conds[i].getPsrType() == null){
				return true;
			}
		}
		return false;
	}
}
