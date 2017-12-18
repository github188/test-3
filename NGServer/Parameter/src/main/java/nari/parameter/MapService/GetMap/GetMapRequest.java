package nari.parameter.MapService.GetMap;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 地图浏览
 * @author zwl
 *
 */
public class GetMapRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1425839801352766391L;

	private String token = "";
	
	/**
	 * 切片ID
	 */
	private String tileId = "";
	
	/**
	 * 切片列号
	 */
	private int row = 0;
	
	/**
	 * 切片行号
	 */
	private int column = 0;
	
	/**
	 * 切片等级
	 */
	private int level = 0;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTileId() {
		return tileId;
	}

	public void setTileId(String tileId) {
		this.tileId = tileId;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean validate() {
//		if(StringUtils.isEmpty(tileId)){
//			return false;
//		}
//		
		if(StringUtils.isEmpty(String.valueOf(row))){  	
			return false;
		}
		
		if(StringUtils.isEmpty(String.valueOf(column))){
			return false;
		}
		
		if(StringUtils.isEmpty(String.valueOf(level))){
			return false;
		}
		return true;
	}
	
}

