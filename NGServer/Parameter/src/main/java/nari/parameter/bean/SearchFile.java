package nari.parameter.bean;

import java.io.Serializable;

public class SearchFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5967684885390638163L;

	private String fileName = "";
	
	private String fileTime = "";
	
	private String fileForecastTime = "";
	
	private String fileForecastType = "";
	
	private String fileType = "";

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileTime() {
		return fileTime;
	}

	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}

	public String getFileForecastTime() {
		return fileForecastTime;
	}

	public void setFileForecastTime(String fileForecastTime) {
		this.fileForecastTime = fileForecastTime;
	}

	public String getFileForecastType() {
		return fileForecastType;
	}

	public void setFileForecastType(String fileForecastType) {
		this.fileForecastType = fileForecastType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	

	
}
