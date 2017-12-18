package nari.SpatialIndex.searcher;

public class LayerBridge {
	
	private byte[] data;
	
	private SubIndexLayer layer;
	
	public LayerBridge(SubIndexLayer layer,byte[] data) {
		this.layer = layer;
		this.data = data;
	}
	
	public byte[] getByte(){
		return data;
	}
	
	public SubIndexLayer getLayer(){
		return layer;
	}
}
