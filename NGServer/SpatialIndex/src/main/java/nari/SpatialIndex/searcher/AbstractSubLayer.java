package nari.SpatialIndex.searcher;

import java.io.File;

import nari.SpatialIndex.geom.Geometry;

public abstract class AbstractSubLayer implements SubIndexLayer {
	
//	private static final int MAXSIZE = 10;
	
	@Override
	public boolean write(Geometry geom,byte[] data) throws Exception {
//		int count = getCount();
//		if(count+1>MAXSIZE){
//			SubIndexLayer[] layers = split();
//			
//			for(SubIndexLayer layer:layers){
//				CoordinateSequence pyseq = new DefaultCoordinateSequence(new double[]{layer.getXMin(),layer.getYMin(),layer.getXMin(),layer.getYMax(),layer.getXMax(),layer.getYMax(),layer.getXMax(),layer.getYMin(),layer.getXMin(),layer.getYMin()});
//				Geometry g = new DefaultPolygon(pyseq);
//				if(!geom.intersects(g)){
//					continue;
//				}
//				
//				layer.write(geom,data);
//			}
//		}else{
			doWrite(data);
//		}
		
		return true;
	}

	@Override
	public ResultSet read() throws Exception {
		return doRead();
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private SubIndexLayer[] split() throws Exception{
		System.out.println(getDisk().getFile().getPath()+"@"+getSubLayerID());
		double xmin = getXMin();
		double ymin = getYMin();
		double xmax = getXMax();
		double ymax = getYMax();
		
		String layerId = getSubLayerID();
		LayerHeader header = getHeader();
		
		header.removeIndex(layerId);
		
		SubIndexLayer layer1 = new GridSubIndexLayer(xmin,ymin,xmin + (xmax-xmin)/2,ymin + (ymax-ymin)/2,0,layerId+"$1",getDisk(),header);
		if(!layer1.exist()){
			layer1.makeLayer();
		}
		header.addIndex(layer1.getSubLayerID(), layer1);
		
		SubIndexLayer layer2 = new GridSubIndexLayer(xmin + (xmax-xmin)/2,ymin,xmax,ymin + (ymax-ymin)/2,0,layerId+"$2",getDisk(),header);
		if(!layer2.exist()){
			layer2.makeLayer();
		}
		header.addIndex(layer2.getSubLayerID(), layer2);
		
		SubIndexLayer layer3 = new GridSubIndexLayer(xmin,ymin + (ymax-ymin)/2,xmin + (xmax-xmin)/2,ymax,0,layerId+"$3",getDisk(),header);
		if(!layer3.exist()){
			layer3.makeLayer();
		}
		header.addIndex(layer3.getSubLayerID(), layer3);
		
		SubIndexLayer layer4 = new GridSubIndexLayer(xmin + (xmax-xmin)/2,ymin + (ymax-ymin)/2,xmax,ymax,0,layerId+"$4",getDisk(),header);
		if(!layer4.exist()){
			layer4.makeLayer();
		}
		header.addIndex(layer4.getSubLayerID(), layer4);
		
		header.store();
		
		ResultSet resultSet = read();
		Result result = null;
		
		while(resultSet!=null && resultSet.hasNext()){
			result = resultSet.next();
			if(result.hasReleation(layer1)){
				layer1.write(result.getGeomtry(), result.getData());
			}
			
			if(result.hasReleation(layer2)){
				layer2.write(result.getGeomtry(), result.getData());
			}
			
			if(result.hasReleation(layer3)){
				layer3.write(result.getGeomtry(), result.getData());
			}
			
			if(result.hasReleation(layer4)){
				layer4.write(result.getGeomtry(), result.getData());
			}
		}
		
		File file = new File(getDisk().getFile().getParentFile(),getSubLayerID());
		file.delete();
		
		return new SubIndexLayer[]{layer1,layer2,layer3,layer4};
	}
	
	protected abstract boolean doWrite(byte[] data) throws Exception;
	
	protected abstract ResultSet doRead() throws Exception;
}
