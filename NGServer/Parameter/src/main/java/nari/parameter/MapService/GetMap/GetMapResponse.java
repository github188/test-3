package nari.parameter.MapService.GetMap;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;

public class GetMapResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3922876600003483102L;

	/**
	 * 切片列号
	 */
	private int row = 0;
	
	/**
	 * 切片行号
	 */
	private int column = 0;
	
	/**
	 * 图片的字节数组
	 */
	private byte[] img = null;
	
	/**
	 * 切片层级
	 */
	private int level = 0;
	
	private ReturnCode code = null;

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

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
}
