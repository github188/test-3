package nari.model.bean;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;
import nari.model.ModelActivator;
import nari.Geometry.Coordinate;
import nari.Geometry.CoordinateSequence;
import nari.Geometry.CoordinateSequenceFactory;
import nari.Geometry.DefaultCoordinateSequence;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryBuilder;
import nari.Geometry.GeometryType;

public class DefaultGeometryDef implements GeometryDef {

	private Geometry geometry = null;
	
	public DefaultGeometryDef(JGeometry geom){
		try {
			geometry = GeometryBuilder.getBuilder().buildGeometry(geom);
		} catch (Exception e) {
			e.printStackTrace();
			geometry = null;
		}
	}
	
	public DefaultGeometryDef(){
		
	}
	
	@Override
	public Geometry getGeometry() {
		return geometry;
	}

	@Override
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public Geometry createGeometry(GeometryType geoType, CoordinateSequence coords) {
		return null;
	}

	@Override
	public CoordinateSequenceFactory getFactory() {
		return null;
	}

	@Override
	public Object getGeometryStruct() {
		Coordinate[] coords = geometry.getCoordinates();
		CoordinateSequence seqs = new DefaultCoordinateSequence(coords);
		JGeometry geom = GeometryBuilder.getBuilder().createGeometry(geometry.getGeometryType(), seqs);
		STRUCT st = null;
		Connection conn = null;
		try {
			conn = ModelActivator.dbAdaptor.getConnection();
			st = JGeometry.store(geom, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return st;
	}

}
