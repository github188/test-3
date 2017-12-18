package nari.parameter.BaseService.GetConnection;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import nari.parameter.convert.AbstractRequest;

/**
 * 建立连接请求参数
 * @author zwl
 *
 */
public class GetConnectionRequest extends AbstractRequest implements Serializable{
	
	public GetConnectionRequest(){
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2404559286047119020L;

	/**
	 * 用户名
	 */
	private String userName = "";
	
	/**
	 * 密码
	 */
	private String password = "";

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean validate() {
		if(StringUtils.isEmpty(userName)){
			return false;
		}
		
		if(StringUtils.isEmpty(password)){
			return false;
		}
		return true;
	}
	
}
