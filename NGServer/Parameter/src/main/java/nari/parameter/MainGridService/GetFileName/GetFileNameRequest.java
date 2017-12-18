package nari.parameter.MainGridService.GetFileName;

import java.io.Serializable;

public class GetFileNameRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5767795491158951960L;
	
	String path = "D:\\Program Files (x86)\\内网通\\孙彬\\邢曦曦\\降水预报";
	
	String type = "";
	
	String time = "";

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
