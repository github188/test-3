package nari.SpatialIndex.index;

import nari.SpatialIndex.loader.Disk;
import nari.SpatialIndex.loader.IndexReader;

public class GridSpliter extends AbstractSpliter {

	private Disk disk = null;
	
//	private IndexReader reader;
	
	public GridSpliter(Disk disk,IndexReader reader) {
		this.disk = disk;
//		this.reader = reader;
	}
	
	@Override
	protected Disk getDisk() throws Exception {
		return disk;
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

}
