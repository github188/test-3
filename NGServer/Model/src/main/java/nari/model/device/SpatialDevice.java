package nari.model.device;

import nari.model.bean.GeometryDef;
import nari.Geometry.Geometry;

public interface SpatialDevice {

	public static final SpatialDevice NONE = new SpatialDevice(){

		@Override
		public GeometryDef getGeometry() {
			return null;
		}

		@Override
		public void setGeometryDef(GeometryDef geometry) {
			
		}

		@Override
		public Geometry buffer(double degree) {
			return null;
		}

		@Override
		public void addGeometry(Geometry geometry) {
			
		}
		
	};
	
	public GeometryDef getGeometry();
	
	public void setGeometryDef(GeometryDef geometry);
	
	public void addGeometry(Geometry geometry);
	
	public Geometry buffer(double degree);
}
