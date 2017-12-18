package nari.SpatialIndex.index;

import nari.SpatialIndex.loader.Disk;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.Record;

public interface Grid {
	
	public boolean read() throws Exception;
	
	public boolean write(IndexLayer layer,Record record) throws Exception;
	
	public Boundary boundary();
	
	public long getGridId();
	
	public int getColumn();
	
	public int getRow();
	
	public boolean hasLayer(IndexLayer layer) throws Exception;
	
	public boolean createLayer(IndexLayer layer) throws Exception;
	
	public Disk getDisk();
	
	public boolean init() throws Exception;
	
	public boolean isInit() throws Exception;
}
