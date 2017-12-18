package nari.SpatialIndex.searcher;

public class SearchKey {

	private Segment seg;
	
	private IndexLayer[] layers;
	
	private SearchKey(Segment seg,IndexLayer[] layers){
		this.seg = seg;
		this.layers = layers;
	}
	
	public Segment getSegment(){
		return seg;
	}
	
	public IndexLayer[] getLayers(){
		return layers;
	}
	
	public static SearchKey key(Segment seg,IndexLayer[] layers){
		return new SearchKey(seg,layers);
	}
	
	public static SearchKey key(Segment seg,IndexLayer layer){
		return new SearchKey(seg,new IndexLayer[]{layer});
	}
}
