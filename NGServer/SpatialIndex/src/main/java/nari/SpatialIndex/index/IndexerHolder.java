package nari.SpatialIndex.index;

import java.util.concurrent.atomic.AtomicReference;

public class IndexerHolder {

	private static final AtomicReference<IndexerHolder> holder = new AtomicReference<IndexerHolder>();
	
	public static IndexerHolder get(){
		if(holder.get()==null){
			IndexerHolder h = new IndexerHolder();
			holder.compareAndSet(null, h);
		}
		return holder.get();
	}
	
	public Indexer getIndexer(String indexName) {
		
		
		return null;
	}
}
