package nari.distribution.TopoAnalysis.bean;

public class DeviceInfo {

	private String psrType = null;
	
	private String psrId = null;
	
	private String psrName = null;

	public DeviceInfo() {
		super();
	}
	
	public DeviceInfo(String psrType, String psrId) {
		super();
		this.psrType = psrType;
		this.psrId = psrId;
	}
	
	public DeviceInfo(String psrType, String psrId, String psrName) {
		super();
		this.psrType = psrType;
		this.psrId = psrId;
		this.psrName = psrName;
	}

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getPsrId() {
		return psrId;
	}

	public void setPsrId(String psrId) {
		this.psrId = psrId;
	}
	
	public String getPsrName() {
		return psrName;
	}

	public void setPsrName(String psrName) {
		this.psrName = psrName;
	}
}
