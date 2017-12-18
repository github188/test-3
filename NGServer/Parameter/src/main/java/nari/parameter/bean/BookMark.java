package nari.parameter.bean;

import java.io.Serializable;

public class BookMark implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2689439306795093955L;

	/**
	 * 书签ID
	 */
	private String bookMarkId = "";
	
	/**
	 * 地图显示范围
	 */
	private MapView view = null;
	
	/**
	 * 书签名称
	 */
	private String bookMarkName = "";

	public String getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(String bookMarkId) {
		this.bookMarkId = bookMarkId;
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
	
}
