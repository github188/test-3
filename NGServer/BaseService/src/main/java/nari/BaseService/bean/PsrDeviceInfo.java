package nari.BaseService.bean;

/**
 * 标注一个设备的信息（设备类型+设备ID）
 * @author pangyu
 *
 */
public class PsrDeviceInfo {

	private String psrType = null;
	
	private String sbId = null;

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getSbId() {
		return sbId;
	}

	public void setSbId(String sbId) {
		this.sbId = sbId;
	}
	
}
