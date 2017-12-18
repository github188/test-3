package nari.SpatialIndex.searcher;

import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.loader.Disk;

public interface LayerHeader {

	public int getLayerCount() throws Exception;
	
	public SubIndexLayer[] getSubLayers() throws Exception;
	
	public String getPath() throws Exception;
	
	public String getLayerID() throws Exception;
	
	public Disk getDisk() throws Exception;
	
	public SubIndexLayer[] match(Geometry geom) throws Exception;
	
	public boolean write(Geometry geom,byte[] data) throws Exception;
	
	public String getIndexName();
	
	public boolean updateIndex(String layerId,SubIndexLayer layer) throws Exception;
	
	public boolean addIndex(String layerId,SubIndexLayer layer) throws Exception;
	
	public boolean removeIndex(String layerId) throws Exception;
	
	public boolean store() throws Exception;
}
