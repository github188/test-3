package nari.parameter.BaseService.DisConnection;

import java.io.Serializable;

/**
 * 断开连接
 * @author zwl
 *
 */
public class DisConnectionRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7934294824677957134L;

	/**
	 * 安全令牌
	 */
	private String token = "";

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
