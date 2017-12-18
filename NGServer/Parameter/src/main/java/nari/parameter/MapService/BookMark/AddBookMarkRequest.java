package nari.parameter.MapService.BookMark;

import java.io.Serializable;

import nari.parameter.bean.MapView;
import nari.parameter.convert.AbstractRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 书签添加
 * @author zwl
 *
 */
public class AddBookMarkRequest extends AbstractRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2604801262897656132L;
	
	private String token = "";
	
	/**
	 * 书签的地图范围
	 */
	private MapView view = null;
	
	/**
	 * 书签名称
	 */
	private String bookMarkName = "";
	
	//当前登录用户名
	private String userName = "";
	
	//用户标识
	private String identify = "";
	
	//图实例
	private int documentId = 0;
	

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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

	public String getBookMarkName() {
		return bookMarkName;
	}

	public void setBookMarkName(String bookMarkName) {
		this.bookMarkName = bookMarkName;
	}
	
	public boolean validate() {
		if(StringUtils.isEmpty(bookMarkName)){
			return false;
		}
		
		if(view == null){  	
			return false;
		}
		
		if(StringUtils.isEmpty(userName)){
			return false;
		}
		if(StringUtils.isEmpty(identify)){
			return false;
		}
		
		if(StringUtils.isEmpty(String.valueOf(documentId))){  	
			return false;
		}

		return true;
	}
	
}
