package nari.SpatialIndex.loader;

public class GridIndexReader extends AbstractIndexReader{
	
	private Disk storage;
	
	public GridIndexReader(Disk storage){
		this.storage = storage;
	}
	
	@Override
	protected boolean doStart() throws Exception {
		return true;
	}

	@Override
	protected boolean doInit() throws Exception {
		return true;
	}

	@Override
	protected boolean doStop() throws Exception {
		return true;
	}

	@Override
	protected Disk getDisk() throws Exception {
		return storage;
	}
}
