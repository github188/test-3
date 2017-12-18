package nari.MemCache;

public class MemoryBytePool implements StringPool {

	private long defaultSize = 0;
	
	private long address = 0;
	
	private long offset = 0;
	
	private MemAllocater allocater;
	
	public MemoryBytePool(long defaultSize,MemAllocater allocater) {
		this.defaultSize = defaultSize;
		this.allocater = allocater;
		try {
			address = allocater.allocateMemory(defaultSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public PoolPointer addPool(final String field, final byte[] data) throws Exception {
		final int count = data.length;
		
		final long addr = address + offset;
		for(int i=0;i<count;i++){
			allocater.putByte(addr + i, data[i]);
			offset ++;
		}
		
		return new PoolPointer() {

			@Override
			public int size() throws Exception {
				return count;
			}

			@Override
			public long address() throws Exception {
				return addr;
			}

			@Override
			public String getFieldName() throws Exception {
				return field;
			}
		};
	}

	@Override
	public boolean hasSpace(long size) throws Exception {
		return defaultSize - offset<size?false:true;
	}

	@Override
	public long size() throws Exception {
		return defaultSize;
	}

}
