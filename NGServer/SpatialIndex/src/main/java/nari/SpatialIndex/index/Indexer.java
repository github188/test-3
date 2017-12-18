package nari.SpatialIndex.index;

import java.net.URI;

import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.searcher.ResultSet;
import nari.SpatialIndex.searcher.SearchKey;

public interface Indexer extends Lifecycle {
	
	public boolean destory() throws Exception;
	
	public boolean isInstalled();
	
	public boolean isInited();
	
	public boolean install() throws Exception;
	
	public boolean isStop();
	
	public IndexType getIndexType();
	
	public String getIndexerName();
	
	public URI getIndexURI();
	
	public ResultSet search(SearchKey key) throws Exception;
	
}
