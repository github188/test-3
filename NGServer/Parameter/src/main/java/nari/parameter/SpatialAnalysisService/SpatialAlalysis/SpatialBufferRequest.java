package nari.parameter.SpatialAnalysisService.SpatialAlalysis;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.TypeCondition;

/**
 * 空间对象缓冲区分析
 * @author zwl
 *
 */
public class SpatialBufferRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7502721859865205581L;

	private String token = "";
	
	/**
	 * 空间信息
	 */
	private GeometryPair geom = null;
	
	/**
	 * 缓冲区半径，度
	 */
	private double radius = 0;
	
	/**
	 * 查询条件
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

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public TypeCondition[] getConds() {
		return conds;
	}

	public void setConds(TypeCondition[] conds) {
		this.conds = conds;
	}
	
	public boolean validate() {
		if(geom == null){
			return false;
		}
		if(StringUtils.isEmpty(String.valueOf(radius))){
			return false;
		}
		for(int i=0;i<conds.length;i++){
			if(conds[i] == null){
				return false;
			}
		}
		return true;
	}
}
