package nari.model.device;

import java.util.Iterator;

import nari.model.bean.ClassDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.SubClassDef;

public interface Device {

	public static final Device NONE = new Device(){

		@Override
		public void setValue(FieldDetail field, Object value) {
			
		}

		@Override
		public void setValue(String name, Object value) {
			
		}

		@Override
		public Object getValue(FieldDetail field) {
			return null;
		}

		@Override
		public Object getValue(String name) {
			return null;
		}

		@Override
		public TopoDevice asTopoDevice() {
			return null;
		}

		@Override
		public SpatialDevice asSpatialDevice() {
			return null;
		}

		@Override
		public boolean isTopoDevice() {
			return false;
		}

		@Override
		public boolean isGeometryDevice() {
			return false;
		}

		@Override
		public boolean isVirtualDevice() {
			return false;
		}

		@Override
		public VirtualDevice asVirtualDevice() {
			return null;
		}

		@Override
		public Iterator<FieldDetail> keyIterator() {
			return null;
		}

		@Override
		public ClassDef getClassDef() {
			return null;
		}

		@Override
		public SubClassDef getSubClassDef() {
			return null;
		}
		
	};
	
	public void setValue(FieldDetail field,Object value);
	
	public void setValue(String name,Object value);
	
	public Object getValue(FieldDetail field);
	
	public Object getValue(String name);
	
	public boolean isTopoDevice();
	
	public boolean isGeometryDevice();
	
	public boolean isVirtualDevice();
	
	public TopoDevice asTopoDevice();
	
	public SpatialDevice asSpatialDevice();
	
	public VirtualDevice asVirtualDevice();
	
	public Iterator<FieldDetail> keyIterator();
	
	public ClassDef getClassDef();
	
	public SubClassDef getSubClassDef();
}
