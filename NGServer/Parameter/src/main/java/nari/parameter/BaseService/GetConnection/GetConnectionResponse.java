package nari.parameter.BaseService.GetConnection;

import java.io.Serializable;

import nari.parameter.bean.LoginUser;
import nari.parameter.bean.MapView;
import nari.parameter.code.ReturnCode;

public class GetConnectionResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7571797333873875541L;

	/**
	 * 系统登录令牌，用于服务调用等一切操作的凭证
	 */
	private String token;
	
	private LoginUser user = null;
	
	/**
	 * 地图初始化的现实范围
	 */
	private MapView view = null;
	
	/**
	 * 返回码值
	 */
	private ReturnCode code = null;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MapView getView() {
		return view;
	}

	public void setView(MapView view) {
		this.view = view;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public LoginUser getUser() {
		return user;
	}

	public void setUser(LoginUser user) {
		this.user = user;
	}
	
}
