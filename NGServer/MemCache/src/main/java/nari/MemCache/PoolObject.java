package nari.MemCache;

public class PoolObject {

	private Object value = null;
	
	private long address = 0;
	
	public PoolObject(Object value){
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getAddress() {
		return address;
	}

	public void setAddress(long address) {
		this.address = address;
	}
	
}
