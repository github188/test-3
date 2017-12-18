package nari.SpatialIndex.searcher;

public interface RecordProcesser<T> {

	public T process(Object value) throws Exception;
}
