package nari.parameter.QueryService.SpatialLocate;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 空间定位
 * @author zwl
 *
 */
public class LocateRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7740623516191489873L;

	private String token = "";
	
	/**
	 * 设备类型
	 */
	private String psrType = "";
	
	/**
	 * 要定位OID集合
	 */
	private String[] oids = null;

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

	public String[] getOids() {
		return oids;
	}

	public void setOids(String[] oids) {
		this.oids = oids;
	}
	
	public boolean validate(){
		for(int i=0;i<oids.length;i++){
			if(oids[i] == null){
				return false;
			}
		}
		if(oids.length == 0){
			return false;
		}
		if(StringUtils.isEmpty(psrType)){
			return false;
		}
		return true;
	}
}
