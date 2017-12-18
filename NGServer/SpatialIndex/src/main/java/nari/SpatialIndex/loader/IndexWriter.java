package nari.SpatialIndex.loader;

import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.index.GridMapper;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.Record;

public interface IndexWriter extends Lifecycle{
	
	public boolean write(IndexLayer layer,Record record,GridMapper mapper) throws Exception;
	
}
