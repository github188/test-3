package nari.model.bean;

import nari.Geometry.CoordinateSequence;
import nari.Geometry.CoordinateSequenceFactory;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryType;

public interface GeometryDef {

	public static final GeometryDef NONE = new GeometryDef(){

		@Override
		public Geometry getGeometry() {
			return null;
		}

		@Override
		public void setGeometry(Geometry geometry) {
			
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
			return null;
		}
		
	};
	
	public Geometry getGeometry();
	
	public void setGeometry(Geometry geometry);
	
	public Geometry createGeometry(GeometryType geoType,CoordinateSequence coords);
	
	public CoordinateSequenceFactory getFactory();
	
	public Object getGeometryStruct();
	
}
