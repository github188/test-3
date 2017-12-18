package nari.BaseService.bean;

public class GetConvexHullRequest {

	/**
	 * 设备类型的体系，classId、equId和modelId都可以作为参数，默认equId
	 */
	private String psrTypeSys = "equId";  //默认台账ID
	
	/**
	 * 设备的列表
	 */
	private PsrDeviceInfo[] psrDevices = null;

	public String getPsrTypeSys() {
		return psrTypeSys;
	}

	public void setPsrTypeSys(String psrTypeSys) {
		this.psrTypeSys = psrTypeSys;
	}

	public PsrDeviceInfo[] getPsrDevices() {
		return psrDevices;
	}

	public void setPsrDevices(PsrDeviceInfo[] psrDevices) {
		this.psrDevices = psrDevices;
	}
	
}
