package nari.parameter.MapService.BookMark;

import java.io.Serializable;

import nari.parameter.bean.BookMark;
import nari.parameter.code.ReturnCode;

public class AddBookMarkResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5079501732228325294L;

	/**
	 * 书签信息
	 */
	private BookMark bookMark = null;
	
	private ReturnCode code = null;

	public BookMark getBookMark() {
		return bookMark;
	}

	public void setBookMark(BookMark bookMark) {
		this.bookMark = bookMark;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
}
