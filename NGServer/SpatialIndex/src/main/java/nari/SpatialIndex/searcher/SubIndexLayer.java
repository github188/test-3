package nari.SpatialIndex.searcher;

import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.loader.Disk;

public interface SubIndexLayer {

	public double getXMax() throws Exception;
	
	public double getYMax() throws Exception;
	
	public double getXMin() throws Exception;
	
	public double getYMin() throws Exception;
	
	public String getSubLayerID() throws Exception;
	
	public int getCount() throws Exception;
	
	public Disk getDisk() throws Exception;
	
	public String getPath() throws Exception;
	
	public boolean write(Geometry geom,byte[] data) throws Exception;
	
	public ResultSet read() throws Exception;
	
	public LayerHeader getHeader() throws Exception;
	
	public boolean exist() throws Exception;
	
	public boolean makeLayer() throws Exception;
	
}
