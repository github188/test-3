package nari.SpatialIndex.handler;

import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.searcher.Record;

public interface IndexerInstallHandler extends Lifecycle{

	public boolean isInit();
	
	public Record next() throws Exception;
	
}
