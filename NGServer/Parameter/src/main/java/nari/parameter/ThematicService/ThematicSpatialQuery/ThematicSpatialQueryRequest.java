package nari.parameter.ThematicService.ThematicSpatialQuery;

import java.io.Serializable;

import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.TypeCondition;

/**
 * 专题图控件查询
 * @author zwl
 *
 */
public class ThematicSpatialQueryRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9080882572284659731L;

	private String token = "";
	
	private String mapType = "";
	
	private String mapId = "";
	
	private GeometryPair geom = null;
	
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

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
}
