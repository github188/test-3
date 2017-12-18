package nari.SpatialIndex.searcher;

public interface ResultSet {

	public boolean hasNext() throws Exception;
	
	public Result next() throws Exception;
}
