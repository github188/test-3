package nari.SpatialIndex.index;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import nari.SpatialIndex.searcher.IndexLayer;

public class LayerHolder {

	private static final AtomicReference<LayerHolder> holder = new AtomicReference<LayerHolder>();
	
	private final ConcurrentMap<String, IndexLayer> layerCache = new ConcurrentHashMap<String, IndexLayer>();
	
	public static LayerHolder get(){
		if(holder.get()==null){
			LayerHolder lh = new LayerHolder();
			holder.compareAndSet(null, lh);
		}
		return holder.get();
	}
	
	public void put(String layerID,IndexLayer layer) throws Exception {
		layerCache.put(layerID, layer);
	}
	
	public IndexLayer get(String layerID) throws Exception {
		return layerCache.get(layerID);
	}
	
	public boolean hasLayer(String layerID) throws Exception {
		return layerCache.containsKey(layerID);
	}
}
