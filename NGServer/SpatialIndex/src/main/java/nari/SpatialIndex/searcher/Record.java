package nari.SpatialIndex.searcher;

import nari.SpatialIndex.geom.Geometry;

public interface Record {

	public String getIndexValue();
	
	public Object getValue(String label);
	
	public Geometry getGeometry();
	
	public IndexLayer getLayer();
	
	public byte[] toByte() throws Exception;
}
