package nari.parameter.MapService.BookMark;

import java.io.Serializable;

import nari.parameter.bean.MapView;
import nari.parameter.convert.AbstractRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 书签修改
 * @author zwl
 *
 */
public class ModifyBookMarkRequest extends AbstractRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1294411515270941661L;

	private String token = "";
	
	/**
	 * 书签地图范围
	 */
	private MapView view = null;
	
	/**
	 * 书签名称
	 */
	private String bookMarkName = "";
	
	/**
	 * 书签ID
	 */
	private String bookMarkId = "";
	
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

	public String getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(String bookMarkId) {
		this.bookMarkId = bookMarkId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean validate() {
		if(StringUtils.isEmpty(bookMarkName)){
			return false;
		}
		
		if(view == null){  	
			return false;
		}
		
		if(StringUtils.isEmpty(bookMarkId)){
			return false;
		}
		return true;
	}
}
