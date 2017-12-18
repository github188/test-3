package nari.SpatialIndex.searcher;

public class Layers {

	public static IndexLayer layer(String layerId,String layerName){
		return new GridIndexLayer(layerId,layerName);
	}
	
}
