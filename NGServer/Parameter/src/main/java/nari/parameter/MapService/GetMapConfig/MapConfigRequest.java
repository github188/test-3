package nari.parameter.MapService.GetMapConfig;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 查询地图切片配置信息
 * @author zwl
 *
 */
public class MapConfigRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5412045471695734602L;

	private String token = "";

	/**
	 * 地图ID，专题图还是地理图
	 */
	private String mapId = "";
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	public boolean validate() {
		if(StringUtils.isEmpty(mapId)){
			return false;
		}
		return true;
	}
}
