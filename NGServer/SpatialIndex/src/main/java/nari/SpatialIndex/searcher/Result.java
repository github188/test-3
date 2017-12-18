package nari.SpatialIndex.searcher;

import nari.SpatialIndex.geom.Geometry;

public interface Result {

	public boolean hasReleation(SubIndexLayer layer) throws Exception;
	
	public Geometry getGeomtry() throws Exception;
	
	public byte[] getData() throws Exception;
}
