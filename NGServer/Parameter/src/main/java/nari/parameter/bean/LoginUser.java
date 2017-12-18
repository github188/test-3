package nari.parameter.bean;

import java.io.Serializable;

public class LoginUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4081261456626646204L;

	/**
	 * 用户ID
	 */
	private String userId = "";
	
	/**
	 * 用户名称
	 */
	private String userName = "";
	
	/**
	 * 登录名
	 */
	private String loginName = "";
	
	/**
	 * 性别
	 */
	private String sex = "";
	
	/**
	 * 用户所属组织结构信息
	 */
	private UserDepartment dept = null;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public UserDepartment getDept() {
		return dept;
	}

	public void setDept(UserDepartment dept) {
		this.dept = dept;
	}
	
}
