package nari.parameter.SpatialAnalysisService.DeviceSpatialAnalysis;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import nari.parameter.bean.TypeCondition;

/**
 * 设备缓冲区分析
 * @author zwl
 *
 */
public class DeviceBufferRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -567724658420851640L;

	private String token = "";
	
	/**
	 * 设备类型
	 */
	private String psrType = "";
	
	/**
	 * 图形ID 
	 */
	private String oid = "";
	
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

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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
		if(StringUtils.isEmpty(psrType)){
			return false;
		}
		if(StringUtils.isEmpty(oid)){
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
