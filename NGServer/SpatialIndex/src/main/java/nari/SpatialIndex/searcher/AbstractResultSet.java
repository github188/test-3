package nari.SpatialIndex.searcher;

import nari.SpatialIndex.geom.CoordinateSequence;
import nari.SpatialIndex.geom.DefaultCoordinateSequence;
import nari.SpatialIndex.geom.DefaultPolygon;
import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.geom.Polygon;

public abstract class AbstractResultSet implements ResultSet {

	private int index =0;
	
	@Override
	public boolean hasNext() throws Exception {
		int count = getResultCount();
		return index<count;
	}

	@Override
	public Result next() throws Exception {
		final SerialObject obj = getObject(index++);
		
		return new Result() {
			
			@Override
			public boolean hasReleation(SubIndexLayer layer) throws Exception {
				Geometry geom = (Geometry)obj.getValue(SerialObject.GEOMETRY);
				CoordinateSequence seq = new DefaultCoordinateSequence(new double[]{layer.getXMin(),layer.getYMin(),layer.getXMax(),layer.getYMin(),layer.getXMax(),layer.getYMax(),layer.getXMin(),layer.getYMax(),layer.getXMin(),layer.getYMin()});
				Polygon g = new DefaultPolygon(seq);
				return geom.intersects(g);
			}
			
			@Override
			public Geometry getGeomtry() throws Exception {
				return (Geometry)obj.getValue(SerialObject.GEOMETRY);
			}
			
			@Override
			public byte[] getData() throws Exception {
				return obj.getData();
			}
		};
	}

	public abstract int getResultCount() throws Exception;
	
	public abstract SerialObject getObject(int index) throws Exception;
}
