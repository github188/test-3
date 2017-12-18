package nari.SpatialIndex.factory;

import java.util.concurrent.atomic.AtomicReference;

import nari.SpatialIndex.index.GridIndexer;
import nari.SpatialIndex.index.IndexAttribute;
import nari.SpatialIndex.index.IndexType;
import nari.SpatialIndex.index.Indexer;

public class IndexerFactory {

	private static final AtomicReference<IndexerFactory> ref = new AtomicReference<IndexerFactory>();
	
	public static IndexerFactory getFactory(){
		if(ref.get()==null){
			IndexerFactory factory = new IndexerFactory();
			ref.compareAndSet(null, factory);
		}
		return ref.get();
	}
	
	public Indexer createIndexer(IndexAttribute attribute) throws Exception {
		if(attribute.getIndexType()==IndexType.GRID){
			return new GridIndexer(attribute);
		}
		return null;
	}
}
