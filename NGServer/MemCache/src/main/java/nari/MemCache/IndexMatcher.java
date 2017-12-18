package nari.MemCache;

public interface IndexMatcher {

	public IndexType match(String key) throws Exception;
	
	public Object translate(Object value) throws Exception;
}
