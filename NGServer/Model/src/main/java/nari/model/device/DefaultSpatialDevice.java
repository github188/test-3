package nari.model.device;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import nari.model.bean.DefaultGeometryDef;
import nari.model.bean.GeometryDef;
import nari.Geometry.Geometry;

public class DefaultSpatialDevice implements SpatialDevice {

	private Device device = Device.NONE;
	
	private final AtomicReference<GeometryDef> ref = new AtomicReference<GeometryDef>(GeometryDef.NONE);
	
	public DefaultSpatialDevice(Device device){
		this.device = device;
	}
	
	@Override
	public GeometryDef getGeometry() {
		if(device == Device.NONE){
			return GeometryDef.NONE;
		}
		
		Object obj = device.getValue("shape");
		if(obj==null){
			return GeometryDef.NONE;
		}
		
		if(ref.get()==GeometryDef.NONE){
			if(obj instanceof Geometry){
				GeometryDef def = new DefaultGeometryDef();
				def.setGeometry((Geometry)obj);
				ref.compareAndSet(GeometryDef.NONE, def);
			}else{
				JGeometry geom = readGeometry(obj);
				if(geom==null){
					return GeometryDef.NONE;
				}
				
				GeometryDef def = new DefaultGeometryDef(geom);
				ref.compareAndSet(GeometryDef.NONE, def);
			}
		}
		
		return ref.get();
	}

	@Override
	public void setGeometryDef(GeometryDef geometry) {
		ref.set(geometry);
		device.setValue("shape", geometry.getGeometryStruct());
	}

	@Override
	public Geometry buffer(double degree) {
		GeometryDef geoDef = getGeometry();
		if(GeometryDef.NONE==geoDef){
			return null;
		}
		Geometry source = geoDef.getGeometry();
		return source.buffer(degree);
	}
	
	private JGeometry readGeometry(Object geoObject){
		STRUCT struct = null;
		Method method = null;
		JGeometry geom = null;
		if ("weblogic.jdbc.wrapper.Struct_oracle_sql_STRUCT".equals(geoObject.getClass().getName())){
			try {
				method = geoObject.getClass().getMethod("getVendorObj", new Class[0]);
				struct = (STRUCT)method.invoke(geoObject, new Object[0]);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			struct = (STRUCT)geoObject;
		}
		try {
			geom = JGeometry.load(struct);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return geom;
	}

	@Override
	public void addGeometry(Geometry geometry) {
		GeometryDef def = new DefaultGeometryDef();
		def.setGeometry(geometry);
		setGeometryDef(def);
	}

}
