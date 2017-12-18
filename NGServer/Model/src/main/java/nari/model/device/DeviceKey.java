package nari.model.device;

public class DeviceKey {

	private int modelId;
	
	private int oid;

	public DeviceKey(int modelId, int oid) {
		super();
		this.modelId = modelId;
		this.oid = oid;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}
}
