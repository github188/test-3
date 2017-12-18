package nari.SpatialIndex.loader;

import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.searcher.IndexLayer;

public interface IndexReader extends Lifecycle{

	public void read(IndexLayer layer) throws Exception;
}
