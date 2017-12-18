package nari.parameter.BaseService.GetPSRDef;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 查询设备类型分类
 * @author zwl
 *
 */
public class GetPSRDefRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6615006956800405332L;
	
	//设备整体分类(6种中的一种)
	private String type = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean validate() {
		if(StringUtils.isEmpty(type)){
			return true;
		}
		return false;
	}
	
}
