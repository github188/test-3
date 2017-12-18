package nari.MemCache;

public class StdMemCluster extends AbstractMemCluster {

	private MemAllocater allocater;
	
	public StdMemCluster(Cache cache,MemAllocater allocater,IndexCluster indexCluster) {
		super(cache,indexCluster);
		this.allocater = allocater;
	}

	@Override
	public MemAllocater getMemAllocater() throws Exception {
		return allocater;
	}

	@Override
	protected boolean doInit() throws Exception {
		return true;
	}

	@Override
	protected boolean doStart() throws Exception {
		return true;
	}

	@Override
	protected boolean doStop() throws Exception {
		return true;
	}

	@Override
	public long sizeOf(Object obj) throws Exception {
		return allocater.sizeOf(obj);
	}
	
	@Override
	protected MemoryBlock newBlock(long size) throws Exception {
		MemoryBlock block = new UnsafeMemoryBlock(allocater,size);
		return block;
	}
	
}
