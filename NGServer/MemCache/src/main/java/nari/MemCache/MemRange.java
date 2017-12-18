package nari.MemCache;

public class MemRange {

	private long address;
	
	private long size;
	
	public MemRange(long address,long size){
		this.address = address;
		this.size = size;
	}
	
	public long getAddress() {
		return address;
	}
	
	public long getSize() {
		return size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		MemRange range = (MemRange)obj;
		
		if(address==range.getAddress() && size == range.getSize()){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)(address*17+size);
	}
}
