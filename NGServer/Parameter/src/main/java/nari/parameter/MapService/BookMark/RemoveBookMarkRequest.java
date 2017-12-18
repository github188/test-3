package nari.parameter.MapService.BookMark;

import java.io.Serializable;

import nari.parameter.convert.AbstractRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 书签删除
 * @author zwl
 *
 */
public class RemoveBookMarkRequest extends AbstractRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7468406872772212618L;

	private String token = "";
	
	/**
	 * 书签ID集合
	 */
	private String[] bookMarkId = null;

	public String[] getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(String[] bookMarkId) {
		this.bookMarkId = bookMarkId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean validate() {
		for(int i = 0;i<bookMarkId.length;i++){
			if(StringUtils.isEmpty(bookMarkId[i])){
				return false;
			}
		}
		return true;
	}
}
