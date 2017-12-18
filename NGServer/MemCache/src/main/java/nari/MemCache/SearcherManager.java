package nari.MemCache;

public class SearcherManager {

	public static Searcher selectSearcher(String op) {
		if(op.equalsIgnoreCase("normal")){
			return new NormalSearcher();
		}else if(op.equalsIgnoreCase("spatial")){
			return new SpatialSearcher();
		}else{
			return new NoneSearcher();
		}
	}
}
