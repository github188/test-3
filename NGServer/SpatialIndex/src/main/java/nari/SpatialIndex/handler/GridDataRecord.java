package nari.SpatialIndex.handler;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.geom.GeometryReader;
import nari.SpatialIndex.searcher.GridIndexLayer;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.Record;
import nari.SpatialIndex.searcher.SerialObject;

public class GridDataRecord implements Record {

	private final AtomicReference<IndexLayer> layer = new AtomicReference<IndexLayer>();
	
	private final AtomicReference<Geometry> geometry = new AtomicReference<Geometry>();
	
	private Map<String,Object> map;
	
	public GridDataRecord(Map<String,Object> map){
		this.map = map;
	}

	@Override
	public IndexLayer getLayer() {
		if(layer.get()==null){
			IndexLayer ly = new GridIndexLayer(String.valueOf(map.get("sbzlx")),String.valueOf(map.get("sbzlx")));
			layer.compareAndSet(null, ly);
		}
		return layer.get();
	}

	@Override
	public String getIndexValue() {
		return String.valueOf(map.get("oid"));
	}

	@Override
	public Geometry getGeometry() {
		Object shp = map.get("shape");
		if(shp==null){
			return null;
		}
		if(geometry.get()==null){
			GeometryReader reader = GeometryReader.getReader();
			Geometry geom = reader.read(shp);
			geometry.compareAndSet(null, geom);
		}
		return geometry.get();
	}

	@Override
	public Object getValue(String label) {
		return map.get(label);
	}

	@Override
	public byte[] toByte() throws Exception {
		SerialObject obj = new SerialObject();
//		obj.setGeometry(getGeometry());
		obj.setValue("geometry", getGeometry());
		for(Map.Entry<String,Object> entry:map.entrySet()){
			if(entry.getKey().equalsIgnoreCase("shape")){
				continue;
			}
			obj.setValue(entry.getKey(), entry.getValue());
		}
		ByteArrayOutputStream st = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(st);
		stream.writeObject(obj);
		
		return st.toByteArray();
	}
	
}
