package nari.parameter.QueryService.VirtualSpatialLocate;

import java.io.Serializable;

/**
 * 虚拟设备定位
 * @author zwl
 *
 */
public class VirtualLocateRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6668769314075191570L;

	private String token = "";
	
	/**
	 * 设备类型
	 */
	private String psrType = "";
	
	/**
	 * 设备图形ID
	 */
	private String oid = "";

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
	
}
