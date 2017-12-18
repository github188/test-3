package nari.parameter.code;

import java.io.Serializable;

/**
 * 返回参数码值定义
 * @author zwl
 *
 */
	public class ReturnCode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1743493889939783453L;

	/**
	 * 码值
	 */
	private int code;
	
	/**
	 * 描述
	 */
	private String message;

	public ReturnCode(int code,String message){
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static final int SUCCESS_CODE = 1000;
	
	public static final int FAILED_CODE = 4000;
	
	public static final int NOUSER_CODE = 4001;
	
	public static final int MISSTYPE_CODE = 4002;
	
	public static final int NULL_CODE = 4003;
	
	public static final int VALUEWRONG_CODE = 4004;
	
	public static final int BUILDMODEL_CODE = 4005;
	
	public static final int SQLERROR_CODE = 4006;
	
	public static final int NODATA_CODE = 4007;
	
	public static final int PRINTERROR_CODE = 4008;
	
	public static final ReturnCode SUCCESS = new ReturnCode(SUCCESS_CODE, "执行成功");
	
	public static final ReturnCode FAILED = new ReturnCode(FAILED_CODE, "执行失败");
	
	public static final ReturnCode NOUSER = new ReturnCode(NOUSER_CODE, "权限错误");
	
	public static final ReturnCode MISSTYPE = new ReturnCode(MISSTYPE_CODE, "传入参数（request属性类别）格式（JSON转换）出错");
	
	public static final ReturnCode NULL = new ReturnCode(NULL_CODE, "传入参数缺少必须值");
	
	public static final ReturnCode VALUEWRONG = new ReturnCode(VALUEWRONG_CODE, "参数值有误（可能不存在对应数据）");
	
	public static final ReturnCode BUILDMODEL = new ReturnCode(BUILDMODEL_CODE, "模型创建时出错");
	
	public static final ReturnCode SQLERROR = new ReturnCode(SQLERROR_CODE, "数据库查询出错");
	
	public static final ReturnCode NODATA = new ReturnCode(NODATA_CODE, "无数据");
	
	public static final ReturnCode PRINTERROR = new ReturnCode(PRINTERROR_CODE, "返回前台打印出错");
	
	public static ReturnCode getCode(int code,String message){
		return new ReturnCode(code,message);
	}
}
