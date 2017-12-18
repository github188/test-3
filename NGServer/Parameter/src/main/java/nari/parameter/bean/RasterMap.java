package nari.parameter.bean;

import java.io.Serializable;

public class RasterMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4697097435057009825L;

	/**
	 * 地图名称
	 */
	private String mapName = "";
	
	/**
	 * 坐标原点X
	 */
	private double ox = 0;
	
	/**
	 * 坐标原点Y
	 */
	private double oy = 0;
	
	/**
	 * 切片图片类型
	 */
	private String imageType = "";
	
	/**
	 * 切片宽度，像素
	 */
	private int imageWidth = 0;
	
	/**
	 * 切片高度，像素
	 */
	private int imageHeight = 0;
	
	/**
	 * 起始层级
	 */
	private int startLevel = 0;
	
	/**
	 * 结束层级
	 */
	private int endLevel = 0;
	
	/**
	 * 切片访问地址
	 */
	private String url = "";

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public double getOx() {
		return ox;
	}

	public void setOx(double ox) {
		this.ox = ox;
	}

	public double getOy() {
		return oy;
	}

	public void setOy(double oy) {
		this.oy = oy;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}

	public int getEndLevel() {
		return endLevel;
	}

	public void setEndLevel(int endLevel) {
		this.endLevel = endLevel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
