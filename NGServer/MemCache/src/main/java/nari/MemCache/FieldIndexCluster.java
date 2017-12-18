package nari.MemCache;

public class FieldIndexCluster extends AbstractIndexCluster {

	private PointerCluster ptrCluster;
	
	public FieldIndexCluster(PointerCluster ptrCluster) {
		this.ptrCluster = ptrCluster;
	}

	@Override
	public PointerCluster getPointerCluster() throws Exception {
		return ptrCluster;
	}
}
